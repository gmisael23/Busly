package com.busly.app.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.busly.app.R;
import com.busly.app.databinding.ItemNotificacaoBinding;
import com.busly.app.model.Notificacao;

import java.util.List;

public class NotificacaoAdapter extends RecyclerView.Adapter<NotificacaoAdapter.ViewHolder> {

    private final List<Notificacao> itens;

    public NotificacaoAdapter(List<Notificacao> itens) {
        this.itens = itens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificacaoBinding binding = ItemNotificacaoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.vincular(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemNotificacaoBinding binding;

        ViewHolder(ItemNotificacaoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void vincular(Notificacao n) {
            binding.tvTitulo.setText(n.getTitulo());
            binding.tvMensagem.setText(n.getMensagem());
            binding.tvHorario.setText(n.getHorario());

            String icone;
            int fundoIcone;
            switch (n.getTipo()) {
                case PROXIMIDADE:
                    icone = "🔔";
                    fundoIcone = R.drawable.bg_icon_laranja;
                    break;
                case EMBARQUE:
                    icone = "✓";
                    fundoIcone = R.drawable.bg_icon_menta;
                    break;
                case DESEMBARQUE:
                    icone = "🚪";
                    fundoIcone = R.drawable.bg_icon_menta;
                    break;
                case ATRASO:
                default:
                    icone = "⚠️";
                    fundoIcone = R.drawable.bg_icon_vermelho;
                    break;
            }
            binding.tvIcone.setText(icone);
            binding.flIcone.setBackgroundResource(fundoIcone);

            // Não lidas ganham destaque colorido; lidas ficam brancas.
            int fundoCard = R.drawable.bg_card_branco;
            if (n.isNaoLida()) {
                fundoCard = (n.getTipo() == Notificacao.Tipo.PROXIMIDADE)
                        ? R.drawable.bg_card_laranja
                        : R.drawable.bg_card_menta;
            }
            binding.cardNotificacao.setBackgroundResource(fundoCard);
        }
    }
}
