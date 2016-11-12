package fr.m1_tlse3.mcs.corpusmic;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by silmathoron on 12/11/2016.
 */

public class CorpusAdapter extends BaseAdapter { //ArrayAdapter<String>
    private List<String> commands = new ArrayList<>();
    private Context mContext;

    public CorpusAdapter(Context context) {
        mContext = context;
        commands = Arrays.asList(mContext.getResources().getStringArray(R.array.commands));
    }

    @Override
    public int getCount() {
        return commands.size();
    }

    @Override
    public String getItem(int position) {
        return commands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        // Notre vue n'a pas encore été construite, nous le faisons
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            v = inflater.inflate(R.layout.command_list_item, parent, false);
        } // Notre vue peut être récupérée, nous le faisons
        else {
            v = convertView;
        }
        ((TextView)v.findViewById(R.id.word)).setText(commands.get(position));
        return v;
    }
    /*Context context;
    int layoutResourceId;
    String commands[] = null;

    public CorpusAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        commands = context.getResources().getStringArray(R.array.commands);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.command_list_item, parent, false);

            holder = new Holder();
            holder.commandWord = (TextView)row.findViewById(R.id.word);

            row.setTag(holder);
        }
        else
        {
            holder = (Holder)row.getTag();
        }

        String command = commands[position];
        holder.commandWord.setText(command);
        Log.d("Adapter", "coucou");

        return row;
    }

    static class Holder
    {
        TextView commandWord;
    }*/
}
