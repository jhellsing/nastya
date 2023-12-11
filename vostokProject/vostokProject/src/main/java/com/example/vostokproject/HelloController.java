package com.example.vostokproject;

import com.example.restoranproject.initializw;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;





public class HelloController implements Initializable {
    //main
    @FXML
    private AnchorPane mainForm;
    @FXML
    private Button warehouse, order, report, exit, motherlode;
    @FXML
    private TextField balance;
    //main

    //searchForm
    @FXML
    private AnchorPane searchForm;
    @FXML
    private Button bup1, bup2, exit2;
    @FXML
    private TextArea swarchBup;
    //searchForm

    //orderForm
    @FXML
    private AnchorPane orderForm;
    @FXML
    private Button exit1, yesBuy, tydaButton, sydaButton;
    @FXML
    private ComboBox post;
    @FXML
    private ListView listV, listV1;
    @FXML
    private Label sumYes, opasnaaa, goodShop;
    @FXML
    private TextField skF;
    //orderForm

    //reportForm
    @FXML
    private AnchorPane reportForm;
    @FXML
    private Button letsgo;
    @FXML
    private ComboBox dataO;
    @FXML
    private Label goodJob;
    //reportForm

    //warningForm
    @FXML
    private AnchorPane warningForm;
    @FXML
    private Button nono, yesyes;
    //warningForm

    String url = "jdbc:mysql://109.126.196.146:3306/vostok";
    String user = "isaeva";
    String password = "julia092004";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "select balance from balance;";
            Statement statement = connection.createStatement();
            ResultSet answ = statement.executeQuery(sql);
            answ.next();
            balance.setText(answ.getString("balance"));
            // searchField.setText("Подключение к базе данных успешно установлено!");
                } catch (SQLException e) {
                // searchField.setText("Ошибка при подключении к базе данных:\n" + e);

                  } catch (ClassNotFoundException e) {
                // searchField.setText("Ошибка драйвера:\n" + e);
                 } catch (Exception e) {
                 // searchField.setText(e.toString());
        }
    }

    @FXML
    private void fullComboBox() {
        post.getItems().clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "select наименование_поставщика from provider_users";
            Statement statement = connection.createStatement();
            ResultSet answ = statement.executeQuery(sql);
            String result = "";
            while (answ.next()) {
                String getProviders = answ.getString("наименование_поставщика");
                post.getItems().add(getProviders);
            }

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void tydaButton_Click() {
        if (skF.equals("")) {
            opasnaaa.setStyle("-fx-color:#d93e3e");
        } else {
            String getPost = listV.getSelectionModel().getSelectedItem().toString();
            String[] columns = getPost.split(", ");
            String productName = columns[0];
            String manufacturer = columns[3];
            double manufacturerInt = Double.parseDouble(manufacturer);
            double skFInt = Double.parseDouble(skF.getText());
            double finalyM = manufacturerInt * skFInt;
            String finalyMS = String.valueOf(finalyM);
            String price = columns[2];

            double sum = Double.parseDouble(sumYes.getText());
            String sum1 = String.valueOf(sum + finalyM);
            sumYes.setText(sum1);
            listV1.getItems().add(getPost + ", " + (skF.getText()) + ", "+ finalyM);
            double pr = Double.parseDouble(sumYes.getText());
            if (pr > 13000) {
                yesBuy.setDisable(false);
            }
            skF.setText("1");
        }
    }

    @FXML
    private void sudaButton_Click() {
        if (skF.equals("")) {
            opasnaaa.setStyle("-fx-color:#d93e3e");
        } else {
            String getPost = listV1.getSelectionModel().getSelectedItem().toString();
            String[] parts = getPost.split(", ");
            double sum = Double.parseDouble(sumYes.getText()) - Double.parseDouble(parts[5].replace("сумма:", ""));
            sumYes.setText(String.valueOf(sum));
            if (getPost != null) {
                listV1.getItems().remove(getPost);
            }
        }
    }

    @FXML
    private void yesBuy_Click() {
        try {
            if (Double.parseDouble(sumYes.getText())<=Double.parseDouble(balance.getText())){
                String getPost = listV1.getItems().toString();
                String[] columns = getPost.split(", ");
                String productName = columns[0]; //наименование_товара
                String post = columns[2]; //наименование_поставщика
                String sumS = columns[3];
                double sum = Double.parseDouble(sumS); //сумма
                String manufacturer = columns[4];
                int man = Integer.parseInt(manufacturer); //количество
                Date currentDate = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String sqlDate = formatter.format(currentDate); //дата
                String getItog = sumYes.getText();
                double itog = Double.parseDouble(getItog);

                Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                Connection connection = DriverManager.getConnection(url, user, password);
                String sql = "insert into ord (наименование_поставщика, наименование_товара, сумма, количество, дата, итого) values ('"+post.replace("[","")+"','"+productName.replace("[","")+"','"+sum+"','"+man+"','"+sqlDate+"','"+itog+"')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                goodShop.setVisible(true);
                yesBuy.setDisable(true);

                double nBalance = Double.parseDouble(balance.getText()) - Double.parseDouble(sumYes.getText());
                Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                connection = DriverManager.getConnection(url, user, password);
                sql = "update balance set balance = '"+ nBalance +"' where id = 0;";
                statement = connection.createStatement();
                statement.executeUpdate(sql);
                balance.setText(String.valueOf(nBalance));
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void output() {
        listV.getItems().clear();
        goodShop.setVisible(false);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            Connection connection = DriverManager.getConnection(url, user, password);
            String getPost = post.getSelectionModel().getSelectedItem().toString();
            if (getPost.isEmpty() || getPost.equals("")) {
            } else {
                String sql = "select наименоване_товара, тип_товара, название_поставщика, цена_товара from provider where название_поставщика = '" + getPost + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet res = preparedStatement.executeQuery();
                while (res.next()) {
                    String name = res.getString("наименоване_товара");
                    String type = res.getString("тип_товара");
                    String provider = res.getString("название_поставщика");
                    float sum = res.getFloat("цена_товара");
                    String resultt = name + ", " + type + ", " + provider + ", " + sum + "\n";
                    listV.getItems().add(resultt);
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void sumClean() {
        skF.setText("");
    }

    @FXML
    private void ordClick() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        dataO.getItems().clear();
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        Connection connection = DriverManager.getConnection(url, user, password);
        String sql = "select дата from ord";
        Statement statement = connection.createStatement();
        ResultSet answ = statement.executeQuery(sql);
        String result = "";
        while (answ.next()) {
            String getProviders = answ.getString("дата");
            dataO.getItems().add(getProviders);
        }
    }
    @FXML
    private void Okay() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        Connection connection = DriverManager.getConnection(url, user, password);
        String getData = dataO.getSelectionModel().getSelectedItem().toString();
        String sql = "SELECT наименование_поставщика, дата, итого FROM ord where дата = '"+getData+"'";
        Statement statement = connection.createStatement();
        ResultSet answ = statement.executeQuery(sql);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Отчет"+getData);

        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("Наименование поставщика");
        headerRow.createCell(1).setCellValue("Дата");
        headerRow.createCell(2).setCellValue("Сумма");
        headerRow.createCell(3).setCellValue("НДС");
        headerRow.createCell(4).setCellValue("Сумма с НДС");

        int rowNum = 1;
        while (answ.next()) {

            String supplier = answ.getString("наименование_поставщика");
            String date = answ.getString("дата");
            float sum = answ.getFloat("итого");

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(supplier);
            row.createCell(1).setCellValue(date);
            row.createCell(2).setCellValue(sum + " руб");
            row.createCell(3).setCellValue("20%");
            row.createCell(4).setCellValue(sum+(sum*0.2) + "руб");
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream outputStream = new FileOutputStream(new File(System.getProperty("user.home") + "/Desktop/отчет за "+getData+".xlsx"))) {
            workbook.write(outputStream);
        }

        goodJob.setVisible(true);

    }

    @FXML
    private void motherlode_Click(){
        try {
            double nBalance = Double.parseDouble(balance.getText()) + 3000;
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "update balance set balance = '"+ nBalance +"' where id = 0;";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            balance.setText(String.valueOf(nBalance));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void setc(){
        warehouse.setStyle("-fx-background-color:#e9967a");
    }
    @FXML
    private void ourc(){
        warehouse.setStyle("-fx-background-color: #f5f5f5");
    }
    @FXML
    private void setc1(){
        order.setStyle("-fx-background-color:#e9967a");
    }
    @FXML
    private void ourc1(){
        order.setStyle("-fx-background-color: #f5f5f5");
    }
    @FXML
    private void setc2(){
        report.setStyle("-fx-background-color:#e9967a");
    }
    @FXML
    private void ourc2(){
        report.setStyle("-fx-background-color: #f5f5f5");
    }
    @FXML
    private void setc3(){
        exit.setStyle("-fx-background-color:#e9967a");
    }
    @FXML
    private void ourc3(){
        exit.setStyle("-fx-background-color: #f5f5f5");
    }
    @FXML
    private void exit_Click(){
        System.exit(1);
    }

    @FXML
    private void warehouse_Click(){
        swarchBup.setText("");
        searchForm.setDisable(false);
        orderForm.setDisable(false);
        mainForm.setVisible(false);
        searchForm.setVisible(true);
        orderForm.setVisible(false);
        reportForm.setVisible(false);
    }
    @FXML
    private void order_Click(){
        listV1.getItems().clear();
        listV.getItems().clear();
        post.getItems().clear();
        searchForm.setDisable(false);
        orderForm.setDisable(false);
        sumYes.setText("0.00");
        mainForm.setVisible(false);
        searchForm.setVisible(false);
        orderForm.setVisible(true);
        reportForm.setVisible(false);
    }
    @FXML
    private void report_Click(){
        dataO.getItems().clear();
        goodJob.setVisible(false);
        mainForm.setVisible(true);
        mainForm.setDisable(true);
        searchForm.setVisible(false);
        orderForm.setVisible(false);
        reportForm.setVisible(true);
    }
    @FXML
    private void exit1_Click(){
        warningForm.setVisible(true);
        mainForm.setDisable(true);
        searchForm.setDisable(true);
        orderForm.setDisable(true);
    }
    @FXML
    private void nono(){
        warningForm.setVisible(false);
        searchForm.setDisable(false);
        orderForm.setDisable(false);
    }
    @FXML
    private void yesyes(){
        warningForm.setVisible(false);
        searchForm.setDisable(false);
        orderForm.setDisable(false);
        mainForm.setVisible(true);
        mainForm.setDisable(false);
        searchForm.setVisible(false);
        orderForm.setVisible(false);
        reportForm.setVisible(false);
    }
    @FXML
    private void bup1_Click() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        Connection connection = DriverManager.getConnection(url, user, password);
        String sql = "select название_товара, тип_товара, поставщик, количество from warehouse where warehouse.тип_товара = 'хоз. часть'";
        Statement statement = connection.createStatement();
        ResultSet answ = statement.executeQuery(sql);
        String result = "";
        while (answ.next()){
            String name = answ.getString("название_товара");
            String type = answ.getString("тип_товара");
            String provider = answ.getString("поставщик");
            int quantity = answ.getInt("количество");
            result += name + ", " + type + ", " + provider + ", " + quantity + "\n";
        }
        swarchBup.setText(result);
    }
    @FXML
    private void bup2_Click() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        Connection connection = DriverManager.getConnection(url, user, password);
        String sql = "select название_товара, тип_товара, поставщик, количество from warehouse where warehouse.тип_товара = 'продукты'";
        Statement statement = connection.createStatement();
        ResultSet answ = statement.executeQuery(sql);
        String result = "";
        while (answ.next()){
            String name = answ.getString("название_товара");
            String type = answ.getString("тип_товара");
            String provider = answ.getString("поставщик");
            int quantity = answ.getInt("количество");
            result += name + ", " + type + ", " + provider + ", " + quantity + "\n";
        }
        swarchBup.setText(result);
    }

    @FXML
    private void motherlode(){
        motherlode.setStyle("-fx-background-color: #dcdcdc");
    }
    @FXML
    private void BBmotherlode(){
        motherlode.setStyle("-fx-background-color: #d93e3e");
    }
    @FXML
    private void bup2(){
        bup2.setStyle("-fx-background-color: #dcdcdc");
    }
    @FXML
    private void BBbup2(){
        bup2.setStyle("-fx-background-color: #d93e3e");
    }
    @FXML
    private void bup1(){
        bup1.setStyle("-fx-background-color: #dcdcdc");
    }
    @FXML
    private void BBbup1(){
        bup1.setStyle("-fx-background-color: #d93e3e");
    }
    @FXML
    private void exit2(){
        exit2.setStyle("-fx-background-color: #dcdcdc");
    }
    @FXML
    private void BBexit2(){
        exit2.setStyle("-fx-background-color: #d93e3e");
    }
    @FXML
    private void exit1(){
        exit1.setStyle("-fx-background-color: #dcdcdc");
    }
    @FXML
    private void BBexit1(){
        exit1.setStyle("-fx-background-color: #d93e3e");
    }
    @FXML
    private void tyda(){
        tydaButton.setStyle("-fx-background-color: #dcdcdc");
    }
    @FXML
    private void bbtyda(){
        tydaButton.setStyle("-fx-background-color: #d93e3e");
    }
    @FXML
    private void yesb(){
        yesBuy.setStyle("-fx-background-color: #dcdcdc");
    }
    @FXML
    private void bbyesb(){
        yesBuy.setStyle("-fx-background-color: #d93e3e");
    }






}