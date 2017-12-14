# Memória

Esta seção apresentará os resultados das métricas acerca do uso da memória. [Android Profiler](https://developer.android.com/studio/profile/android-profiler.html) e [LeakCanary](https://github.com/square/leakcanary) foram utilizados para esta etapa.

# Profiling do uso de memória

Usando o [Android Profiler](https://developer.android.com/studio/profile/android-profiler.html), foi avaliado o uso de memória durante uma sessão do app (download da lista de episódios, interação com o fluxo do aplicativo, download e reprodução de um episódio). Entretanto, isso causou um impacto mínimo no aumento de memória alocada pelo aplicativo em si (o que era alocado era coletado quando o _garbage collector_ era acionado; quando houve aumento, era no eixo `Graphics` e `Native` (laranja e azul no gráfico, respectivamente).

![Saída do Android Profiler](https://raw.githubusercontent.com/dijckstra/exercicio-podcast/assets/memory-1.png) 

# Detecção de vazamentos de memória usando o LeakCanary

O [LeakCanary](https://github.com/square/leakcanary) é uma ferramenta de monitoramento que detecta vazamentos de memória. Um vazamento de memória significa que existem objetos na memória que, após terem sido usados, deveriam ser _garbage collected_, mas não o são porque ainda existe uma cadeia de referências que estão ligadas a ele (Java não coleta objetos se eles ainda possuírem uma referência). Se isto persistir, pode ocorrer um `OutOfMemoryError`.

Para que o LeakCanary seja utilizado, o projeto precisa ser configurado; depois, caso exista algum objeto suspeito de causar um leak, usa-se o `RefWatcher` para monitorar o objeto. [Este commit](https://github.com/dijckstra/exercicio-podcast/commit/cf0d49a4a09a1839d90a2727c95349faa6a52f29) introduz as mudanças descritas; como o LeakCanary monitora automaticamente as Activities, apenas as Fragments e o MediaPlaybackService foram adicionadas manualmente.

O LeakCanary envia uma notificação caso exista algum vazamento; no caso deste projeto, nenhum foi encontrado, como mostra a imagem abaixo.

![Notificação do LeakCanary, indicando que não houve vazamento](https://raw.githubusercontent.com/dijckstra/exercicio-podcast/assets/memory-2.png)