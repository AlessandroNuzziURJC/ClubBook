package clubbook.backend.service;

import clubbook.backend.dtos.AttendanceDto;
import clubbook.backend.dtos.ClassGroupAttendanceDto;
import clubbook.backend.dtos.UserAttendanceDto;
import clubbook.backend.model.Attendance;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.User;
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

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, UserService userService, ClassGroupService classGroupService) {
        this.attendanceRepository = attendanceRepository;
        this.userService = userService;
        this.classGroupService = classGroupService;
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
        }
        if (!userSet.isEmpty()) {
            throw new RuntimeException();
        }
        attendanceRepository.saveAll(attendanceList);
        return attendanceDto;
    }

    public Attendance saveAttendance(Attendance attendance) {
        this.attendanceRepository.save(attendance);
        return attendance;
    }

    private List<UserAttendanceDto> getUserAttendanceDtos(int year, int month, int classGroup) {
        List<Object[]> results = attendanceRepository.findAllUserAttendanceDtoRaw(year, month, classGroup);
        return results.stream().map(result ->
                new UserAttendanceDto(
                        (Integer) result[0],
                        (String) result[1],
                        (String) result[2],
                        Arrays.asList((Boolean[]) result[3])
                )
        ).collect(Collectors.toList());
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

            // Bucle para recorrer los meses del año (0 = Enero, 11 = Diciembre)
            for (int month = 0; month < 12; month++) {
                document.newPage();

                String monthName = this.getSpanishMonth(month );

                document.add(new Paragraph("Mes: " + monthName + "\n"));

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

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }

    public void deleteAll() {
        this.attendanceRepository.deleteAll();
    }
}
