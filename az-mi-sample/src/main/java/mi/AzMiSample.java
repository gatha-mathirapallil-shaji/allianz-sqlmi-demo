package mi;

import java.io.*;
import java.net.*;
import com.fasterxml.jackson.core.*;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

public class AzMiSample {
    public static void main(String[] args) throws Exception {

        URL msiEndpoint = new URL(
                "http://169.254.169.254/metadata/identity/oauth2/token?api-version=2018-02-01&resource=https://database.windows.net/");
        HttpURLConnection con = (HttpURLConnection) msiEndpoint.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Metadata", "true");

        if (con.getResponseCode() != 200) {
            throw new Exception("Error calling managed identity token endpoint.");
        }

        InputStream responseStream = con.getInputStream();

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(responseStream);

        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();

            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fieldName = parser.getCurrentName();
                jsonToken = parser.nextToken();

                if ("access_token".equals(fieldName)) {
                    String accesstoken = parser.getValueAsString();
                    System.out.println("Access Token: " + accesstoken.substring(0, 5) + "..."
                            + accesstoken.substring(accesstoken.length() - 5));

                    // Connect with the access token.
                    SQLServerDataSource ds = new SQLServerDataSource();

                    ds.setServerName("hcmssqlazmi-d-fc1-sqlmidev-0fw.84c51f047608.database.windows.net"); // Replace with your server name.
                    ds.setDatabaseName("testaml_db"); // Replace with your database name.
		   ds.setAccessToken(accesstoken);
      		   // ds.setAuthentication("ActiveDirectoryMSI");

      try (Connection connection = ds.getConnection();
                            Statement stmt = connection.createStatement();
                            ResultSet rs = stmt.executeQuery("SELECT * from recipes ORDER BY recipe_id")) { // replace with another table name based on your database

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        while (rs.next()) {
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1)
                                    System.out.print(",  ");
                                String columnValue = rs.getString(i);
                                System.out.print(columnValue + " " + rsmd.getColumnName(i));
                            }
                            System.out.println("");
                        }
                        if (rs.next()) {
                            System.out.println("You have successfully logged on as: " + rs.getString(1));
                        }
                    }

                    return;
                }
            }
        }
    }
}
