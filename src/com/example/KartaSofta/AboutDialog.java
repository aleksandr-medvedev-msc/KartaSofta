package com.example.kartasofta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.kartasofta.catalog.R;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 4/11/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AboutDialog extends DialogFragment implements View.OnClickListener{
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("О компании");
        View v = inflater.inflate(R.layout.dialog_about, null);
        TextView wb = (TextView) v.findViewById(R.id.dialog_about_webview);
        wb.setText(XmlParser.AboutParse(getActivity()));
        //wb.loadUrl("http://kartasofta.ru/xml_yandex/contact_to_android.php");
        v.findViewById(R.id.dialog_about_button_go).setOnClickListener(this);
        v.findViewById(R.id.dialog_about_button_ok).setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dialog_about_button_ok:
                dismiss();
                break;
            case R.id.dialog_about_button_go:
                Uri uri = Uri.parse("http://kartasofta.ru");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(browserIntent);
                break;
            default:
                break;
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}