# Performance e velocidade do aplicativo

Esta seção exibirá métricas colhidas pelo [Android Profiler](https://developer.android.com/studio/profile/android-profiler.html) durante o uso do aplicativo, observando o uso da CPU durante a sessão.

## Histórico de uso da CPU

A sessão de uso foi capturada por completo (usando o `Record method trace`). Destaca-se o uso do `AsyncTask` para o download do XML e a atualização da `RecyclerView` quando ele é movido pelo usuário; de resto, não houve anomalias. Especificamente, o download do XML também envolve a inserção dos episódios no banco de dados, mas percebe-se um overhead maior que o comum pois, na implementação, insere-se os itens um a um, o que é custoso para um banco de dados (devido à transação de IO).

![Saída do Android Profiler para a implementação antiga](https://raw.githubusercontent.com/dijckstra/exercicio-podcast/assets/cpu-1.png)

Portanto, fez-se uma alteração no código para que os episódios fossem inseridos em lotes; O impacto na aplicação foi significativo, reduzindo o tempo de execução. [Este commit](https://github.com/dijckstra/exercicio-podcast/commit/8153d4773b0291806dadb221bbc625ec3794c859) introduz as mudanças.

![Saída do Android Profiler para a implementação nova](https://raw.githubusercontent.com/dijckstra/exercicio-podcast/assets/cpu-2.png)