import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeAnalysis {
    public static void main(String[] args) {
        
        String inputFilePath = "Assignment_Timecard.csv";

        try (FileReader fileReader = new FileReader(inputFilePath);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                String positionID = record.get("Position ID");
                String positionStatus = record.get("Position Status");
                String time = record.get("Time");
                String timeOut = record.get("Time Out");
                double timecardHours = Double.parseDouble(record.get("Timecard Hours (as Time)"));
                String payCycleStartDate = record.get("Pay Cycle Start Date");
                String payCycleEndDate = record.get("Pay Cycle End Date");
                String employeeName = record.get("Employee Name");
                String fileNumber = record.get("File Number");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

               
                int consecutiveDaysCount = 0;
                Date currentDate = dateFormat.parse(payCycleStartDate);
                for (int i = 0; i < 7; i++) {
                    Date nextDate = dateFormat.parse(record.get("Pay Cycle End Date"));
                    if (isConsecutiveDays(currentDate, nextDate)) {
                        consecutiveDaysCount++;
                        currentDate = nextDate;
                    } else {
                        break;
                    }
                }

                if (consecutiveDaysCount == 7) {
                    System.out.println("Employee Name: " + employeeName + ", Position: " + positionID + ", Worked for 7 consecutive days");
                }

                
                if (timecardHours < 10 && timecardHours > 1) {
                    System.out.println("Employee Name: " + employeeName + ", Position: " + positionID + ", Less than 10 hours between shifts");
                }

                if (timecardHours > 14) {
                    System.out.println("Employee Name: " + employeeName + ", Position: " + positionID + ", Worked more than 14 hours in a single shift");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    
    private static boolean isConsecutiveDays(Date date1, Date date2) {
        long difference = date2.getTime() - date1.getTime();
        return difference == 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    }
}
