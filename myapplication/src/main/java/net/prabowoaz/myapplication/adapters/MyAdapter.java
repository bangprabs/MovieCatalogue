package net.prabowoaz.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.prabowoaz.myapplication.Film;
import net.prabowoaz.myapplication.R;
import net.prabowoaz.myapplication.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<Film> films;


    private OnItemClickListener onItemClickListener;

    public MyAdapter(Context context) {
        this.context = context;
        films = new ArrayList<>();
    }

    public void addAll(List<Film> films) {
        this.films.addAll(films);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Film getData(int position) {
        return films.get(position);
    }

    public void clear() {
        films.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @NonNull
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(films.get(position));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtDesc, txtRealese;
        private ImageView imgPhoto;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false));
            initViews();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null)
                        onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

        public void bind(Film film) {
            txtTitle.setText(film.getTitle());
            txtDesc.setText(film.getOverview().length() > 100 ? film.getOverview().subSequence(0, 100) + "..." :
                    film.getOverview());

            Glide.with(context).load("https://image.tmdb.org/t/p/w500" + film.getPoster_path()).into(imgPhoto);
            txtRealese.setText(film.getRelease_date() != null ? film.getRelease_date() : film.getRelease_date());
        }

        public void initViews() {
            txtTitle = itemView.findViewById(R.id.titleMovie);
            txtDesc = itemView.findViewById(R.id.descMovie);
            imgPhoto = itemView.findViewById(R.id.posterMovie);
            txtRealese = itemView.findViewById(R.id.releaseDate);
        }
    }
}
