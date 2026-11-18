package com.busly.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.busly.app.R;
import com.busly.app.data.RepositorioMock;
import com.busly.app.databinding.FragmentHistoryBinding;
import com.busly.app.model.Aluno;
import com.busly.app.model.RegistroViagem;
import com.busly.app.util.Chips;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/** Tela "Histórico": filtre por aluno e toque em um dia para ver os detalhes. */
public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoricoAdapter adapter;
    private List<Aluno> alunos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alunos = RepositorioMock.alunos();
        binding.tvFiltro1.setText(alunos.get(0).getRotulo());
        binding.tvFiltro2.setText(alunos.get(1).getRotulo());
        binding.tvFiltro1.setOnClickListener(v -> selecionar(0));
        binding.tvFiltro2.setOnClickListener(v -> selecionar(1));

        adapter = new HistoricoAdapter(
                RepositorioMock.historico(alunos.get(0).getNome()), this::mostrarDetalhes);
        binding.rvHistorico.setAdapter(adapter);
        Chips.estilizar(binding.tvFiltro1, true);
        Chips.estilizar(binding.tvFiltro2, false);
    }

    private void selecionar(int indice) {
        Chips.estilizar(binding.tvFiltro1, indice == 0);
        Chips.estilizar(binding.tvFiltro2, indice == 1);
        adapter.atualizar(RepositorioMock.historico(alunos.get(indice).getNome()));
    }

    private void mostrarDetalhes(RegistroViagem registro) {
        String mensagem = getString(R.string.detalhes_viagem,
                registro.getHoraEmbarque(),
                registro.getHoraDesembarque(),
                registro.getDuracaoMinutos());
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(registro.getData())
                .setMessage(mensagem)
                .setPositiveButton(R.string.fechar, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
