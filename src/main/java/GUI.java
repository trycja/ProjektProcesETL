import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class GUI extends Application {

    @FXML
    Button e;
    @FXML
    Button t;
    @FXML
    Button l;
    @FXML
    Button etl;
    @FXML
    Button csv;
    @FXML
    Button clear;
    @FXML
    Button show;

    @FXML
    TextField tf1;
    @FXML
    TextArea ta1;
    @FXML
    TextArea ta2;

    Stage classStage = new Stage();
    String kodProduktu = "";
    String ceneo = "";
    String page = "-";
    String url = ceneo + kodProduktu + page;
    String poczatkowyUrl = "";
    String urlStrony = "/";
    int liczbaOpinii = 0;

    @FXML
    private void procesE() {
        t.setDisable(false);
        e.setDisable(true);
        createExtract();
    }

    @FXML
    private void procesT(javafx.event.ActionEvent event) throws IOException {
        l.setDisable(false);
        t.setDisable(true);
        createTransform();
    }

    @FXML
    private void procesL(javafx.event.ActionEvent event) throws IOException {
        e.setDisable(false);
        l.setDisable(true);
        createLoad();
    }

    @FXML
    private void procesETL(javafx.event.ActionEvent event) throws IOException {
        createExtract();
        createTransform();
        createLoad();
    }

    @FXML
    private void csv(javafx.event.ActionEvent event) throws IOException {
        createCSV();
    }

    @FXML
    private void clear(javafx.event.ActionEvent event) throws IOException {
        cleanDatabase();
    }

    @FXML
    private void show() {
        ta1.appendText(selectOpinion());
    }

    @FXML
    private void ok(javafx.event.ActionEvent event) throws IOException {
        kodProduktu = tf1.getText();
        createURL(kodProduktu);
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public void createCSV() {
        if (!kodProduktu.equals("")) {
            int numerStrony = 0;
            int stop = 0;
            Document doc = null;
            try {
                doc = Jsoup.connect(poczatkowyUrl).followRedirects(false).get();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Elements nastepny = doc.select("[href*=opinie-2]");
            String[][] opinie = new String[100000][12];
            String[] produkt = new String[5];
            produkt[0] = kodProduktu;
            int numerOpinii = 0;
            int id = 0;

            String product = doc.select("meta[name=keywords]").get(0).attr("content");
            String[] parts = product.split(",");

            String dodatkoweUwagi = doc.getElementsByClass("product-param js_family-row").text();

            String[] rodzaj = parts[1].split(" ");

            produkt[1] = rodzaj[1];                                                                    //rodzaj
            produkt[2] = doc.select("meta[property=og:brand]").get(0).attr("content").toString();   //marka
            produkt[3] = parts[0];                                                                    //model
            if (dodatkoweUwagi.length() != 0) produkt[4] = dodatkoweUwagi;
            else produkt[4] = "brak dodatkowych uwag";

            PrintWriter pwp = null;
            try {
                pwp = new PrintWriter(new File("C:\\Grupa27\\produktCSV.csv"), "UTF-8");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            StringBuilder sbp = new StringBuilder();

            for (int x = 0; x < 5; x++) {
                sbp.append(produkt[x]);
                sbp.append(',');
            }
            pwp.write(sbp.toString());
            pwp.close();


            while (stop == 0) {
                if (nastepny.size() == 0)
                    stop = 1;

                // Sprawdzenie czy znajduje sie nastepna strona opini
                for (Element link : nastepny) {
                    String elementyDalej = "";
                    elementyDalej += trim(link.text(), 8);
                    if (trim(link.text(), 8).contains("Następna")) {
                        numerStrony++;
                        urlStrony = url + Integer.toString(numerStrony);

                        Elements test = doc.getElementsByClass("review-box js_product-review");

                        for (Element e : test) {
                            opinie[id][0] = String.valueOf(id);
                            opinie[id][1] = e.select(".reviewer-name-line").first().text(); // autor
                            opinie[id][2] = e.select(".product-review-body").first().text(); // opiniepodsumowanie
                            opinie[id][3] = e.select(".pros-cell").first().select("ul").text(); // zalety
                            opinie[id][4] = e.select(".cons-cell").first().select("ul").text(); // wady
                            opinie[id][5] = e.select("span.review-score-count").first().text(); // ocena
                            opinie[id][6] = e.select("span.review-time").first().text(); // datawystawienia
                            if (e.select(".product-recommended").first() != null)
                                opinie[id][7] = e.select(".product-recommended").first().text();
                            else
                                opinie[id][7] = "nie polecam";
                            // polecam/nie polecam
                            opinie[id][8] = e.getElementsByClass("vote-yes js_product-review-vote js_vote-yes").first()
                                    .text(); // pomocne
                            opinie[id][9] = e.getElementsByClass("vote-no js_product-review-vote js_vote-no").first()
                                    .text(); // niepomocne
                            opinie[id][10] = kodProduktu;
                            id++;
                            liczbaOpinii++;
                        }

                        try {
                            doc = Jsoup.connect(urlStrony).timeout(5000).get();
                            nastepny = doc.select("[href*=Opinie-" + (numerStrony + 1) + "]");
                        } catch (IOException a) {
                            System.out.println("Błąd połączenia");
                        }
                        System.out.println("strona: " + urlStrony);
                    }
                }

                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new File("C:\\Grupa27\\opinieCSV.csv"), "UTF-8");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();

                for (int x = 0; x < id; x++) {
                    for (int j = 0; j < 11; j++) {
                        sb.append(opinie[x][j]);
                        sb.append(',');
                    }
                    sb.append('\n');
                }
                pw.write(sb.toString());
                pw.close();

            }
            ta2.setText("Dodano: " + String.valueOf(liczbaOpinii));
        }
    }


    public void createExtract() {
        if (!kodProduktu.equals("")) {
            int numerStrony = 0;
            int stop = 0;
            Document doc = null;
            try {
                doc = Jsoup.connect(poczatkowyUrl).followRedirects(false).get();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Elements nastepny = doc.select("[href*=opinie-2]");
            String[][] opinie = new String[100000][12];
            String[] produkt = new String[5];
            produkt[0] = kodProduktu;
            int numerOpinii = 0;
            int id = 0;

            String product = doc.select("meta[name=keywords]").get(0).attr("content");
            String[] parts = product.split(",");

            String dodatkoweUwagi = doc.getElementsByClass("product-param js_family-row").text();

            String[] rodzaj = parts[1].split(" ");

            produkt[1] = rodzaj[1];                                                                    //rodzaj
            produkt[2] = doc.select("meta[property=og:brand]").get(0).attr("content").toString();   //marka
            produkt[3] = parts[0];                                                                    //model
            if (dodatkoweUwagi.length() != 0) produkt[4] = dodatkoweUwagi;
            else produkt[4] = "brak dodatkowych uwag";

            PrintWriter pwp = null;
            try {
                pwp = new PrintWriter(new File("C:\\Grupa27\\produkt.csv"), "UTF-8");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            StringBuilder sbp = new StringBuilder();

            for (int x = 0; x < 5; x++) {
                sbp.append(produkt[x]);
                sbp.append('|');
            }
            pwp.write(sbp.toString());
            pwp.close();


            while (stop == 0) {
                if (nastepny.size() == 0)
                    stop = 1;

                // Sprawdzenie czy znajduje sie nastepna strona opini
                for (Element link : nastepny) {
                    String elementyDalej = "";
                    elementyDalej += trim(link.text(), 8);
                    if (trim(link.text(), 8).contains("Następna")) {
                        numerStrony++;
                        urlStrony = url + Integer.toString(numerStrony);

                        Elements test = doc.getElementsByClass("review-box js_product-review");

                        for (Element e : test) {
                            opinie[id][0] = String.valueOf(id);
                            opinie[id][1] = e.select(".reviewer-name-line").first().text(); // autor
                            opinie[id][2] = e.select(".product-review-body").first().text(); // opiniepodsumowanie
                            opinie[id][3] = e.select(".pros-cell").first().select("ul").text(); // zalety
                            opinie[id][4] = e.select(".cons-cell").first().select("ul").text(); // wady
                            opinie[id][5] = e.select("span.review-score-count").first().text(); // ocena
                            opinie[id][6] = e.select("span.review-time").first().text(); // datawystawienia
                            if (e.select(".product-recommended").first() != null)
                                opinie[id][7] = e.select(".product-recommended").first().text();
                            else
                                opinie[id][7] = "nie polecam";
                            // polecam/nie polecam
                            opinie[id][8] = e.getElementsByClass("vote-yes js_product-review-vote js_vote-yes").first()
                                    .text(); // pomocne
                            opinie[id][9] = e.getElementsByClass("vote-no js_product-review-vote js_vote-no").first()
                                    .text(); // niepomocne
                            opinie[id][10] = kodProduktu;
                            id++;
                            liczbaOpinii++;
                        }

                        try {
                            doc = Jsoup.connect(urlStrony).timeout(5000).get();
                            nastepny = doc.select("[href*=Opinie-" + (numerStrony + 1) + "]");
                        } catch (IOException a) {
                            System.out.println("Błąd połączenia");
                        }
                        System.out.println("strona: " + urlStrony);
                    }
                }

                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new File("C:\\Grupa27\\opinie.csv"), "UTF-8");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();

                for (int x = 0; x < id; x++) {
                    for (int j = 0; j < 11; j++) {
                        sb.append(opinie[x][j]);
                        sb.append('|');
                    }
                    sb.append('\n');
                }
                pw.write(sb.toString());
                pw.close();

            }
            ta2.setText("Dodano: " + String.valueOf(liczbaOpinii));
        }
    }

    public void createTransform() {
        try {
            Runtime rt = Runtime.getRuntime();

            Process pr = rt.exec("C:\\Program Files (x86)\\Microsoft SQL Server\\140\\DTS\\Binn\\dtexec /f C:\\Transform.dtsx");

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public void createLoad() {
        try {
            Runtime rt = Runtime.getRuntime();

            Process pr = rt.exec("C:\\Program Files (x86)\\Microsoft SQL Server\\140\\DTS\\Binn\\dtexec /f C:\\Load.dtsx");

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }

    public void cleanDatabase() {
        try {
            Runtime rt = Runtime.getRuntime();

            Process pr = rt.exec("C:\\Program Files (x86)\\Microsoft SQL Server\\140\\DTS\\Binn\\dtexec /f C:\\Truncate.dtsx");

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public String selectOpinion() {

        String opinieProdukt = "";

        try {
            Runtime rt = Runtime.getRuntime();

            Process pr = rt.exec("sqlcmd -Q \"select * from warehouse.dbo.opinie where kodProduktu = " + kodProduktu + "\"");

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                char prev = 'a';
                String linia = "";
                for (char c : line.toCharArray()) {
                    if (!(c == ' ' && prev == ' ')) linia += String.valueOf(c);
                    prev = c;
                }
                opinieProdukt = opinieProdukt + linia + "\n";
            }

            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        System.out.println(opinieProdukt);
        return opinieProdukt;
    }

    public void createURL(String kodProduktu) {
        ceneo = "https://www.ceneo.pl/";
        page = "/opinie-";
        url = ceneo + kodProduktu + page;
        poczatkowyUrl = ceneo + kodProduktu + "#tab=reviews";
        urlStrony = "https://www.ceneo.pl/";
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            classStage = primaryStage;
            primaryStage.setTitle("Proces ETL Ceneo.pl");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hd.fxml"));
            Parent root = fxmlLoader.load();
            tf1 = (TextField) fxmlLoader.getNamespace().get("tf1");

            ta1 = (TextArea) fxmlLoader.getNamespace().get("ta1");
            ta2 = (TextArea) fxmlLoader.getNamespace().get("ta2");
            ta1.setText("");

            e = (Button) fxmlLoader.getNamespace().get("e");
            t = (Button) fxmlLoader.getNamespace().get("t");
            l = (Button) fxmlLoader.getNamespace().get("l");
            etl = (Button) fxmlLoader.getNamespace().get("etl");
            csv = (Button) fxmlLoader.getNamespace().get("csv");
            clear = (Button) fxmlLoader.getNamespace().get("clear");
            l.setDisable(true);
            t.setDisable(true);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
