package com.busly.app.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.busly.app.databinding.ItemHistoricoBinding;
import com.busly.app.model.RegistroViagem;

import java.util.ArrayList;
import java.util.List;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.ViewHolder> {

    /** Chamado quando o usuário toca em um registro. */
    public interface AoClicar {
        void aoClicar(RegistroViagem registro);
    }

    private final List<RegistroViagem> itens = new ArrayList<>();
    private final AoClicar listener;

    public HistoricoAdapter(List<RegistroViagem> itens, AoClicar listener) {
        this.itens.addAll(itens);
        this.listener = listener;
    }

    /** Troca a lista exibida (usado ao mudar o aluno selecionado). */
    @SuppressLint("NotifyDataSetChanged")
    public void atualizar(List<RegistroViagem> novos) {
        itens.clear();
        itens.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoricoBinding binding = ItemHistoricoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegistroViagem registro = itens.get(position);
        holder.binding.tvData.setText(registro.getData());
        holder.binding.tvHoraEmbarque.setText(registro.getHoraEmbarque());
        holder.binding.tvHoraDesembarque.setText(registro.getHoraDesembarque());
        holder.itemView.setOnClickListener(v -> listener.aoClicar(registro));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemHistoricoBinding binding;

        ViewHolder(ItemHistoricoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
