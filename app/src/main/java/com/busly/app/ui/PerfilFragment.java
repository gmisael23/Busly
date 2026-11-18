package com.busly.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.busly.app.MainActivity;
import com.busly.app.data.RepositorioMock;
import com.busly.app.databinding.FragmentPerfilBinding;
import com.busly.app.model.Aluno;
import com.busly.app.util.Configuracoes;
import com.busly.app.util.Sessao;

import java.util.List;

/** Tela "Perfil": dados do responsável, alunos, avisos e sair. */
public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvAvatarGrande.setText(RepositorioMock.RESPONSAVEL_INICIAIS);
        binding.tvNomeResponsavel.setText(RepositorioMock.RESPONSAVEL);
        binding.tvEmail.setText(Sessao.getEmail(requireContext()));

        List<Aluno> alunos = RepositorioMock.alunos();
        binding.tvPerfilAluno1.setText(alunos.get(0).getRotulo());
        binding.tvPerfilAluno2.setText(alunos.get(1).getRotulo());

        binding.swProximidade.setChecked(Configuracoes.avisoProximidade(requireContext()));
        binding.swChegada.setChecked(Configuracoes.avisoChegada(requireContext()));
        binding.swProximidade.setOnCheckedChangeListener((b, marcado) ->
                Configuracoes.setAvisoProximidade(requireContext(), marcado));
        binding.swChegada.setOnCheckedChangeListener((b, marcado) ->
                Configuracoes.setAvisoChegada(requireContext(), marcado));

        binding.btnSair.setOnClickListener(v ->
                ((MainActivity) requireActivity()).confirmarSaida());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
