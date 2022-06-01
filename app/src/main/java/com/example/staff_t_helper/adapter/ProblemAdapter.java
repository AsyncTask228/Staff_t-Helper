package com.example.staff_t_helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staff_t_helper.R;
import com.example.staff_t_helper.domain.Problem;
import com.example.staff_t_helper.nodb.NoDb;

import java.util.List;

public class ProblemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<Problem> problemList;

    public ProblemAdapter(Context context, List<Problem> problemList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.problemList = problemList;
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        private TextView id, person_name, person_surname, edit_num_room, edit_num_bed, problem;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id_problem);
            person_name = itemView.findViewById(R.id.person_name);
            person_surname = itemView.findViewById(R.id.person_surname);
            edit_num_room = itemView.findViewById(R.id.edit_num_room);
            edit_num_bed = itemView.findViewById(R.id.edit_num_bed);
            problem = itemView.findViewById(R.id.problem);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.problems_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Problem problem = NoDb.PROBLEM_LIST.get(position);

        ((MyHolder) holder).id.setText(String.valueOf(problem.getId()));
        ((MyHolder) holder).problem.setText(problem.getName());
        ((MyHolder) holder).person_name.setText(problem.getPerson().getName());
        ((MyHolder) holder).person_surname.setText(problem.getPerson().getSurname());
        ((MyHolder) holder).edit_num_room.setText(String.valueOf(problem.getPerson().getRoom_number()));
        ((MyHolder) holder).edit_num_bed.setText(String.valueOf(problem.getPerson().getBed_number()));

    }

    @Override
    public int getItemCount() {
        return NoDb.PROBLEM_LIST.size();
    }
}
