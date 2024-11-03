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

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserService userService;
    private final ClassGroupService classGroupService;
    private final SeasonService seasonService;
    private final NotificationService notificationService;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, UserService userService, ClassGroupService classGroupService, SeasonService seasonService, NotificationService notificationService) {
        this.attendanceRepository = attendanceRepository;
        this.userService = userService;
        this.classGroupService = classGroupService;
        this.seasonService = seasonService;
        this.notificationService = notificationService;
    }


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

    private List<LocalDate> getClassDates(int month, int classGroup) {
        List<java.sql.Date> sqlDates = attendanceRepository.getClassDates(month, classGroup);
        return sqlDates.stream()
                .map(java.sql.Date::toLocalDate)
                .collect(Collectors.toList());
    }

    private Boolean getUserAttendance(int userId, LocalDate date) {
        Attendance attendance = this.attendanceRepository.findAttendance(userId, date);
        if (attendance == null) {
            return null;
        }
        return attendance.isAttended();
    }

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


    private String getSpanishMonth(int monthPosition){
        String[] months = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        return months[monthPosition];
    }

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

    public void deleteAll() {
        this.attendanceRepository.deleteAll();
    }
}
