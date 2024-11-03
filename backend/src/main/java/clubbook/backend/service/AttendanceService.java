package clubbook.backend.service;

import clubbook.backend.dtos.AttendanceDto;
import clubbook.backend.dtos.ClassGroupAttendanceDto;
import clubbook.backend.dtos.UserAttendanceDto;
import clubbook.backend.model.*;
import clubbook.backend.model.notification.AttendanceNotificationFactory;
import clubbook.backend.model.notification.NotificationFactory;
import clubbook.backend.repository.AttendanceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling attendance-related operations.
 * This includes saving attendance records, generating attendance reports,
 * and managing attendance notifications.
 */
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserService userService;
    private final ClassGroupService classGroupService;
    private final SeasonService seasonService;
    private final NotificationService notificationService;

    /**
     * Constructs an AttendanceService with the specified dependencies.
     *
     * @param attendanceRepository the repository for attendance records
     * @param userService the service for user-related operations
     * @param classGroupService the service for class group-related operations
     * @param seasonService the service for season-related operations
     * @param notificationService the service for managing notifications
     */
    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, UserService userService, ClassGroupService classGroupService, SeasonService seasonService, NotificationService notificationService) {
        this.attendanceRepository = attendanceRepository;
        this.userService = userService;
        this.classGroupService = classGroupService;
        this.seasonService = seasonService;
        this.notificationService = notificationService;
    }

    /**
     * Saves attendance records for a given attendance DTO.
     *
     * @param attendanceDto the attendance data transfer object containing attendance information
     * @return the saved attendance DTO
     * @throws RuntimeException if some users in the class group are not accounted for in the attendance records
     */
    public AttendanceDto saveAll(AttendanceDto attendanceDto) {
        User user;
        Attendance attendance;
        List<Attendance> attendanceList = new ArrayList<>(30);
        ClassGroup classGroup = classGroupService.findById(attendanceDto.getClassGroup());
        Set<User> userSet = new HashSet<>(classGroup.getStudents());
        for (int userId: attendanceDto.getUsersIdsAttended()) {
            user = userService.findById(userId);
            userSet.remove(user);
            attendance = new Attendance();
            attendance.setUser(user);
            attendance.setAttendanceDate(attendanceDto.getDate());
            attendance.setAttended(true);
            attendanceList.add(attendance);
        }
        for (int userId: attendanceDto.getUsersIdsNotAttended()) {
            user = userService.findById(userId);
            userSet.remove(user);
            attendance = new Attendance();
            attendance.setUser(user);
            attendance.setAttendanceDate(attendanceDto.getDate());
            attendance.setAttended(false);
            attendanceList.add(attendance);
            NotificationFactory notificationFactory = new AttendanceNotificationFactory(attendanceDto.getDate(), user);
            notificationFactory.createNotification();
            this.notificationService.save(notificationFactory.getNotification());
        }
        if (!userSet.isEmpty()) {
            throw new RuntimeException();
        }
        attendanceRepository.saveAll(attendanceList);
        return attendanceDto;
    }

    /**
     * Retrieves class dates for a specified month and class group.
     *
     * @param month the month for which class dates are requested
     * @param classGroup the ID of the class group
     * @return a list of LocalDate objects representing class dates
     */
    private List<LocalDate> getClassDates(int month, int classGroup) {
        List<java.sql.Date> sqlDates = attendanceRepository.getClassDates(month, classGroup);
        return sqlDates.stream()
                .map(java.sql.Date::toLocalDate)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the attendance status of a user for a specific date.
     *
     * @param userId the ID of the user
     * @param date the date for which attendance is being checked
     * @return Boolean indicating attendance status (true for attended, false for not attended, null if not found)
     */
    private Boolean getUserAttendance(int userId, LocalDate date) {
        Attendance attendance = this.attendanceRepository.findAttendance(userId, date);
        if (attendance == null) {
            return null;
        }
        return attendance.isAttended();
    }

    /**
     * Gets the attendance records for a class group for a specified year and month.
     *
     * @param month the month for which attendance records are requested
     * @param classGroupId the ID of the class group
     * @return a ClassGroupAttendanceDto containing the attendance records
     */
    public ClassGroupAttendanceDto getClassGroupAttendanceWithYearAndMonth(int month, int classGroupId) {
        List<LocalDate> dates = this.getClassDates(month, classGroupId);
        ClassGroup classGroup = classGroupService.findById(classGroupId);
        List<User> userList = classGroup.getStudents();
        List<UserAttendanceDto> userAttendanceDtoList = new ArrayList<>(userList.size());
        for (User user : userList){
            userAttendanceDtoList.add(new UserAttendanceDto(user.getId(), user.getFirstName(), user.getLastName(), new ArrayList<>()));
        }
        for (LocalDate date : dates) {
            for (UserAttendanceDto userAttendanceDto : userAttendanceDtoList){
                userAttendanceDto.getAttendanceList().add(this.getUserAttendance(userAttendanceDto.getId(), date));
            }
        }

        return new ClassGroupAttendanceDto(dates, userAttendanceDtoList);
    }

    /**
     * Returns the name of the month in Spanish based on the month position.
     *
     * @param monthPosition the zero-based index of the month (0 for January, 1 for February, etc.)
     * @return the name of the month in Spanish
     */
    private String getSpanishMonth(int monthPosition){
        String[] months = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        return months[monthPosition];
    }

    /**
     * Generates a PDF report of attendance for a specific class group.
     *
     * @param classGroupId the ID of the class group for which the report is generated
     * @return a byte array containing the PDF data
     */
    public byte[] generatePdf(int classGroupId) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            LocalDate date = this.seasonService.seasonActive().getInit();

            int monthValue = date.getMonthValue();
            int year = date.getYear();

            for (int month = monthValue - 1; month < 12; month++) {
                this.generateDataForMonth(classGroupId, document, month, year);
            }
            for (int month = 0; month < monthValue - 1; month++) {
                this.generateDataForMonth(classGroupId, document, month, year + 1);
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }

    /**
     * Generates data for a specific month and adds it to the provided PDF document.
     *
     * @param classGroupId the ID of the class group for which data is generated
     * @param document the PDF document to which the data will be added
     * @param month the month for which data is generated (0-based index)
     * @param year the year for which data is generated
     * @throws DocumentException if an error occurs while adding data to the document
     */
    private void generateDataForMonth(int classGroupId, Document document, int month, int year) throws DocumentException {
        document.newPage();

        String monthName = this.getSpanishMonth(month);

        document.add(new Paragraph("Mes: " + monthName + " (" + year + ")\n"));

        ClassGroupAttendanceDto classGroupDto = this.getClassGroupAttendanceWithYearAndMonth(
                month + 1, classGroupId);

        if (classGroupDto == null || classGroupDto.getDatesList().isEmpty()) {
            document.add(new Paragraph("No hay datos para el mes de " + monthName));
        } else {
            float[] columnWidths = new float[classGroupDto.getDatesList().size() + 1];
            columnWidths[0] = 3f;
            for (int i = 1; i < columnWidths.length; i++) {
                columnWidths[i] = 1f;
            }

            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            PdfPCell headerNames = new PdfPCell(new Paragraph("Nombres"));
            headerNames.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerNames.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerNames);

            PdfPCell header;
            for (LocalDate localDate : classGroupDto.getDatesList()) {
                header = new PdfPCell(new Paragraph(String.valueOf(localDate.getDayOfMonth())));
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(header);
            }


            for (UserAttendanceDto user : classGroupDto.getUsersList()) {
                PdfPCell nameCell = new PdfPCell(new Paragraph(user.getFirstName() + " " + user.getLastName()));
                nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                nameCell.setFixedHeight(20f);
                table.addCell(nameCell);


                for (Boolean attended : user.getAttendanceList()) {
                    PdfPCell attendanceCell;
                    if (attended == null) {
                        attendanceCell = new PdfPCell(new Paragraph("-"));
                    } else if (attended) {
                        attendanceCell = new PdfPCell(new Paragraph("A"));
                    } else {
                        attendanceCell = new PdfPCell(new Paragraph("F"));
                    }
                    attendanceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    attendanceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    attendanceCell.setFixedHeight(20f);
                    table.addCell(attendanceCell);
                }
            }

            document.add(table);
        }
    }

    /**
     * Deletes all attendance records from the repository.
     * This method is typically used for data cleanup or resetting attendance data.
     */
    public void deleteAll() {
        this.attendanceRepository.deleteAll();
    }
}
