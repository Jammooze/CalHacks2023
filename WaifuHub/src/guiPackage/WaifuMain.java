package guiPackage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WaifuMain extends Application {

    public static void main(String[] args) throws JSONException {
    	// Launch GUI Application Window
        Application.launch(args);
    }
    
    public static String chatGPT(String message, String personality) throws JSONException {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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"; // API KEY
        String model = "gpt-3.5-turbo"; // Utilizing GPT3.5 Turbo model

        try {
            // Create the HTTP POST request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            // Build the request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"system\", \"content\": \"You are " + personality + "\"}, {\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Extracting the assistant's message content
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray choices = jsonResponse.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject choice = choices.getJSONObject(0);
                JSONObject messageObject = choice.getJSONObject("message");
                return messageObject.getString("content");
            }
            return "Unable to retrieve response.";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private String formatResponse(String response, int lineBreakInterval) {
        StringBuilder formattedResponse = new StringBuilder();
        int count = 0;

        for (char c : response.toCharArray()) {
            if (c == ' ') {
                count++;
                if (count % lineBreakInterval == 0) {
                    formattedResponse.append("\n");
                }
            }
            formattedResponse.append(c);
        }

        return formattedResponse.toString();
    }


    // JavaFX GUI Layout
    @Override
    public void start(Stage primaryStage) {
        try {
            Image flowerFall = new Image("https://i.pinimg.com/originals/05/59/94/05599424bb5a1b7819956d0288559697.gif");
            ImageView flowerFallView = new ImageView(flowerFall);

            BackgroundImage background = new BackgroundImage(
                    flowerFall,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(
                            BackgroundSize.AUTO,
                            BackgroundSize.AUTO,
                            false,
                            false,
                            true,
                            true
                    )
            );

            BorderPane borPane = new BorderPane();
            borPane.setBackground(new Background(background));

            Image gifImage = new Image("https://media.tenor.com/MYnXfWrSWgoAAAAi/miku-hatsune-hatsune-miku.gif");
            ImageView gifImageView = new ImageView(gifImage);
            double desiredWidth = 150;
            double desiredHeight = 150;
            gifImageView.setFitWidth(desiredWidth);
            gifImageView.setFitHeight(desiredHeight);
            
            VBox gifAndText = new VBox();
            gifAndText.setAlignment(Pos.CENTER);
            gifAndText.setPadding(new Insets(0, 20, 20, 20));
            
            Text outputText = new Text();
            
            gifAndText.getChildren().addAll(outputText, gifImageView);
            

            Text askText = new Text("Ask your waifu: ");
            TextField askTextField = new TextField();
            askTextField.setMaxWidth(100);

            Button displayButton = new Button("Ask");
            
            displayButton.setOnAction(e -> {
                try {
                    String response = chatGPT(askTextField.getText(), "Hatsune Miku the cute anime girl, copy the way she talks.");
                    String formattedResponse = formatResponse(response, 10);
                    outputText.setText(formattedResponse);
                    outputText.setFont(Font.font("Comic Sans MS", 20));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            });

            
            HBox textPanel = new HBox();
            textPanel.setAlignment(Pos.CENTER);
            textPanel.setSpacing(20);
            textPanel.getChildren().addAll(askText, askTextField, displayButton);
            textPanel.setPadding(new Insets(0, 20, 20, 20));
            
            HBox taskPanel = new HBox();
            taskPanel.setAlignment(Pos.CENTER_LEFT);
            
            Text taskText = new Text("Tasks for Today <3");
            taskText.setFont(Font.font ("Impact", 30));
            taskText.setFill(Color.PINK);
            
            Button addTaskButton = new Button("+"); // Adding Task Button
            
            addTaskButton.setOnAction(event -> { // Event-handler for addTaskButton
            	
            	// Create a TextInputDialog to prompt the user
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("New Task");
                dialog.setHeaderText("Please enter your answer");
                dialog.setContentText("Answer:");
                
                // Display the dialog and wait for user input
                dialog.showAndWait().ifPresent(answer -> {
                    // Process the user's answer
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Answer");
                    alert.setHeaderText("You entered:");
                    alert.setContentText(answer);
                    alert.showAndWait();
                });
                
            });

            taskPanel.getChildren().addAll(taskText);
            taskPanel.setPadding(new Insets(10, 10, 10, 10));
            
            borPane.setTop(taskPanel);
            borPane.setLeft(addTaskButton);
            borPane.setCenter(gifAndText);
            borPane.setBottom(textPanel);

            Scene scene = new Scene(borPane, 800, 800);
            primaryStage.setTitle("Waifu Hub");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}