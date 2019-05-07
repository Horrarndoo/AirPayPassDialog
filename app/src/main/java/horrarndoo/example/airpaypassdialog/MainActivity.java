package horrarndoo.example.airpaypassdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Horrarndoo on 2019/5/7.
 * <p>
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayDialog();
            }
        });
    }

    private void showPayDialog() {
        final PayPassDialog dialog = new PayPassDialog(this);
        dialog.getPayViewPass().setPayClickListener(new PayPassView.OnPayClickListener() {
            @Override
            public void onPassFinish(String passContent) {
            }

            @Override
            public void onPayClose() {
                dialog.dismiss();
            }

            @Override
            public void onPayForget() {
                dialog.dismiss();
            }
        });
    }
}
