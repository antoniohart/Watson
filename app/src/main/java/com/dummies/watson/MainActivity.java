package com.dummies.watson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConversationService myConversationService =
                new ConversationService(
                        "2017-05-26",
                        getString(R.string.username),
                        getString(R.string.password)
                );
        final TextView conversation = (TextView)findViewById(R.id.conversation);
        final TextView conversation1 = (TextView)findViewById(R.id.conversation1);
        final EditText userInput = (EditText)findViewById(R.id.user_input);

        userInput.setOnEditorActionListener(new TextView
                .OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView tv, int action, KeyEvent keyEvent) {
                if(action == EditorInfo.IME_ACTION_DONE) {
                    final String inputText = userInput.getText().toString();
                    conversation.append(Html.fromHtml("<p style=\\\"text-align: center\\><b >You:</b> " + inputText + "</p>")
                    );

// Optionally, clear edittext
                    userInput.setText("");


                    //send to the conversion service

                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(inputText)
                            .build();

                    ///////////////////////////////

                    myConversationService.message(getString(R.string.workspace), request)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    final String outputText = response.getText().get(0);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            conversation.append(Html.fromHtml("<p ><b> Bot:</b> " +
                                                            outputText + "</p>")
                                            );
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Exception e) {}
                            });
                }


                return false;

            }





        });


    }
}
