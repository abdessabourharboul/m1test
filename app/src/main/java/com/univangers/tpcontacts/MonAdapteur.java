package com.univangers.tpcontacts;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MonAdapteur extends RecyclerView.Adapter<MonAdapteur.myViewHolder> {

    public ArrayList<Contact> m_data = new ArrayList<>();
    private Context m_context;

    public MonAdapteur(Context context) {
        m_context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(m_context).inflate(R.layout.line, null);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        Contact current = m_data.get(position);
        holder.tv_nom.setText(current.nom);
        holder.tv_prenom.setText(current.prenom);
        holder.tv_numTel.setText(current.numTel);
        holder.itemView.setTag(position);
    }

    public ArrayList<Contact> getM_data() {
        return m_data;
    }

    @Override
    public int getItemCount() {
        return m_data.size();
    }

    public void saveBundle(Bundle b) {
        b.putParcelableArrayList("my_data", m_data);
    }

    public void setData(ArrayList<Contact> passedData) {
        m_data = passedData;
        notifyDataSetChanged();
    }

    public void ajouter(String nom, String prenom, String numTel) {
        m_data.add(new Contact(nom, prenom, numTel));
        this.notifyItemInserted(m_data.size() - 1);
    }

    public void supprimer(int position) {
        m_data.remove(position);
        this.notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public static class Contact implements Parcelable {

        public static final Creator CREATOR = new Creator() {
            public Contact createFromParcel(Parcel in) {
                return new Contact(in);
            }

            public Contact[] newArray(int size) {
                return new Contact[size];
            }
        };
        public String nom;
        public String prenom;
        public String numTel;

        public Contact(String nom, String prenom, String numTel) {
            this.nom = nom;
            this.prenom = prenom;
            this.numTel = numTel;
        }

        public Contact(Parcel in) {
            nom = in.readString();
            prenom = in.readString();
            numTel = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(nom);
            dest.writeString(prenom);
            dest.writeString(numTel);
        }
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_nom;
        public TextView tv_prenom;
        public TextView tv_numTel;

        public myViewHolder(View itemView) {
            super(itemView);
            tv_nom = itemView.findViewById(R.id.tv_nom);
            tv_prenom = itemView.findViewById(R.id.tv_prenom);
            tv_numTel = itemView.findViewById(R.id.tv_numTelephone);
        }
    }

}
