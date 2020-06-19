package cdd.equationgenerater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class elemAdapter extends RecyclerView.Adapter<elemAdapter.ViewHolder> {

    private List<elem> elems;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView qId, equ, ans;
        public ViewHolder(View view) {
            super(view);
            layout = (ConstraintLayout)view.findViewById(R.id.itemview);
            qId = (TextView)view.findViewById(R.id.txt_qid);
            equ = (TextView)view.findViewById(R.id.txt_equation);
            ans = (TextView)view.findViewById(R.id.txt_ans);
        }
    }

    public elemAdapter(List<elem> _elems) {
        elems = _elems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elem_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        elem ele = elems.get(position);
        holder.qId.setText("题目" + Integer.toString(position + 1));
        holder.equ.setText(ele.eq);
        holder.ans.setText(ele.ans);
    }

    @Override
    public int getItemCount() {
        return elems.size();
    }
}
