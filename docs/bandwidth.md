# Uso da bateria

Diversos fatores afetam o nível de bateria do aparelho, desde requisições à rede ou com o uso de recusrsos do SO, como o uso de wake locks. Esta seção apresentará o histórico de uso de bateria do aparelho, durante uma sessão do aplicativo, usando o [batterystats/Battery Historian](https://developer.android.com/studio/profile/battery-historian.html)

## Passos para a coleta de métricas de uso de bateria

Primeiro, deve-se obter o Battery Historian, que permite uma visualização, via browser, dos dados de bateria coletados pelo dispositivo. Para isso, é necessária a execução de uma imagem usando o Docker:

```
docker run -d -p 8001:9999 gcr.io/android-battery-historian:2.1 --port 9999 
```

O comando acima irá executar uma imagem do webservice `android-battery-historian:2.1`; agora será possível acessar a página no endereço `localhost:8001`.

Como o dispositivo coleta dados de bateria constantemente, o próximo passo consiste em descartar os dados coletados até então, pois eles não são relevantes para a execução do aplicativo nos próximos passos:

```
adb shell dumpsys batterystats --reset
```

Daí é só usar o aplicativo, realizando as diferentes funcionalidades para que o impacto na bateria seja observado. Feito isso, é necessário resgatar os dados coletados e gerar um bug report:

```
adb shell dumpsys batterystats > batterystats.txt
adb bugreport > bugreport.zip
```

Finalmente, acesse a página do BatteryHistorian (criado pelo Docker) e adicione o bug report.

## Uso da bateria durante uma sessão do apliicativo

A imagem abaixo apresenta uma sessão de uso do aplicativo, que durou cerca de meia hora e consistiu em abrir o aplicativo pela primeira vez (e consequentemente acessando a rede e baixando a lista de episódios do podcast), seguido do download de um episódio e de sua reprodução por completo; todo o fluxo foi feito com o aplicativo em primeiro plano e com o aparelho ligado. Cada linha exibe um segmento onde um componente do sistema está ativo, e consequentemente, consumindo bateria. O gráfico não mostra o quanto da bateria foi usado pelo componente; apenas que o aplicativo estava ativo. O eixo horontal representa o tempo decorrido da sessão.