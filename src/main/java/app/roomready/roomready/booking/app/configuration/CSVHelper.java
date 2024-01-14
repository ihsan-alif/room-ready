package app.roomready.roomready.booking.app.configuration;

import app.roomready.roomready.booking.app.entity.Approval;
import app.roomready.roomready.booking.app.entity.EquipmentNeeds;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {

    private CSVHelper(){
        throw new IllegalArgumentException("illegal Exception");
    }

    public static ByteArrayInputStream tutorialsToCSV(List<Approval> tutorials) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Approval tutorial : tutorials) {
                List<String> data = Arrays.asList(
                        String.valueOf(tutorial.getId()),
                        String.valueOf(tutorial.getReservation().getEmployee().getName()),
                        String.valueOf(tutorial.getAcceptanceStatus()),
                        String.valueOf(tutorial.getReservation().getRoom().getStatus()),
                        String.valueOf(tutorial.getApprovalDate())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream approvalToCSV(List<Approval> approvals) {
        final CSVFormat format = CSVFormat.DEFAULT
                .withQuoteMode(QuoteMode.MINIMAL)
                .withHeader("id", "approval_date", "approved_by", "acceptance_status", "reservationId",
                        "employeeName", "roomName", "reservation_date", "status", "equipmentId", "equipmentName", "quantity");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {

            for (Approval approval : approvals) {
                List<String> data = Arrays.asList(
                        String.valueOf(approval.getId()),
                        approval.getApprovalDate().toString(),
                        approval.getApprovedBy(),
                        approval.getAcceptanceStatus().toString(),
                        String.valueOf(approval.getReservation().getId()),
                        approval.getReservation().getEmployee().getName(),
                        approval.getReservation().getRoom().getName(),
                        approval.getReservation().getReservationDate().toString(),
                        approval.getReservation().getStatus().toString(),
                        getEquipmentId(approval.getReservation().getEquipmentNeeds()),
                        getEquipmentName(approval.getReservation().getEquipmentNeeds()),
                        String.valueOf(approval.getReservation().getQuantity())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException("Fail to import data to CSV file: " + e.getMessage());
        }
    }

    private static String getEquipmentId(EquipmentNeeds equipmentNeeds) {
        return equipmentNeeds != null ? String.valueOf(equipmentNeeds.getId()) : "";
    }

    private static String getEquipmentName(EquipmentNeeds equipmentNeeds) {
        return equipmentNeeds != null ? equipmentNeeds.getName() : "";
    }
}
