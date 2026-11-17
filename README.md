# Busly — Transporte Escolar Inteligente

Aplicativo Android (Java, sem Kotlin) baseado no banner do 6º Workshop de Educação Continuada em TI (UNICID).
Reproduz as 4 telas do protótipo: **Login**, **Início (rota em tempo real)**, **Alertas** e **Histórico**.

## Como abrir
1. Android Studio → **File > Open** → selecione a pasta `Busly`.
2. Aguarde o **Gradle Sync** (JDK 17 — o JDK embutido do Android Studio serve).
3. Rode em um emulador ou celular (Android 8.0 / API 26 ou superior).

Login de teste: qualquer e-mail válido + senha com 6 ou mais caracteres.

## Estrutura
```
app/src/main/java/com/busly/app/
├── LoginActivity.java        Figura 1 – tela de login
├── MainActivity.java         Container + navegação inferior
├── ui/
│   ├── HomeFragment.java     Figura 2 – rota, ETA, status, próximo ponto
│   ├── AlertsFragment.java   Figura 3 – notificações
│   ├── HistoryFragment.java  Figura 4 – histórico de embarque/desembarque
│   ├── RouteMapView.java     View customizada: mapa estilizado + ônibus animado
│   ├── NotificacaoAdapter.java
│   └── HistoricoAdapter.java
├── model/                    Aluno, Notificacao, RegistroViagem
├── data/RepositorioMock.java Dados simulados (iguais ao banner)
└── util/Sessao.java          Sessão local (SharedPreferences)
```

## Próximos passos (conforme o banner)
- Trocar `RouteMapView` pelo SDK do Google Maps e `RepositorioMock` por GPS/banco reais.
- Notificações push (Firebase Cloud Messaging) para proximidade, embarque/desembarque e atraso.
- Autenticação real e cadastro.
