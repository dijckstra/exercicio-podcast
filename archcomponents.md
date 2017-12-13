# Architecture components

Este arquivo documentará o processo de implementação dos _Architecture Components_ no projeto. Os requisitos exigidos incluem a mudança do acesso ao banco de dados `SQLite` (acessado através do `SqliteOpenHelper`) para a biblioteca de persistência de dados [Room](https://developer.android.com/training/data-storage/room/index.html) e um caso de uso que envolva a aplicação de [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html).

## Room

Esta seção descreverá os passos que foram realizados para alterar a implementação antiga do banco de dados (usando `SQLiteDatabase` e sua classe de conveniência `SqliteOpenHelper`) para a implementação da bibloteca de persistência de dados Room. Room fornece uma maneira fácil de converter objetos Java para dados de um banco relacional e prover uma interface bastante simplificada para persistência de itens.

[Este commit](https://github.com/dijckstra/exercicio-podcast/commit/181612b0badd1c2ceb7a9efe077b7c01a1195c09) contém as mudanças feitas para que a aplicação passe a usar Room.

### Passo 1: Identificar entidades

No projeto de Podcasts, a camada de dados possui apenas um repositório - o de episódios. Portanto, a classe `Podcast` é a entidade a ser convertida usando a anotação `@Entity`.
Alguns atributos precisaram ser anotados com `@ColumnInfo`, pois o nome da coluna na tabela é diferente do nome do atributo. Além disso, getters e setters precisaram ser gerados, para que a biblioteca funcione normalmente.

### Passo 2: Criação do DAO (Data Access Object)

Cada método da interface `PodcastLocalDataSource` corresponde a uma _query_ no banco de dados. Portanto, a interface `PodcastsDao` possui os mesmos métodos anotados com uma _query_ que representa a operação realizada no banco de dados. Por exemplo, o seguinte método atualiza o episódio no bandco de dados com o caminho do arquivo baixado:

```
void setPodcastUri(long podcastId, String uri);
```

Este metódo, na interface do DAO, se transforma em:

```
@Query("UPDATE episodes SET state = :state WHERE _id = :podcastId")
int setPodcastState(long podcastId, int state);
```

### Passo 3: Criação do `PodcastsDatabase`

`PodcastsDatabase` é o substituto da classe `PodcastDatabaseHelper`. Assim como o _helper_, ele é um _singleton_, e possui apenas um atributo `PodcastDao` (caso houvesse mais entidades, cada uma teria um DAO e cada DAO seria um atributo desta classe). A classe é anotada com `@Database`, que recebe a lista das classes entidade e a versão. `PodcastsDatabase` será usada em `PodcastProvider`, que é o `ContentProvider` acesado pela aplicação.

### Passo 4: Substituir `PodcastDatabaseHelper` pelo `PodcastsDatabase`

O último passo consiste em substituir o código que utiliza o `PodcastDatabaseHelper` por chamadas do `PodcastsDatabase`. A quantidade de código _boilerplate_ é reduzida, como no exemplo seguinte:

*Com* `PodcastDatabaseHelper`
```
@Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                retCursor = databaseHelper.getReadableDatabase().query(
                        PodcastPersistenceContract.PodcastEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PODCAST_ITEM:
                String[] where = {uri.getLastPathSegment()};
                retCursor = databaseHelper.getReadableDatabase().query(
                        PodcastPersistenceContract.PodcastEntry.TABLE_NAME,
                        projection,
                        PodcastPersistenceContract.PodcastEntry._ID + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
```

*Com* `PodcastsDatabase`
```
@Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (uriMatcher.match(uri)) {
            case PODCAST:
                retCursor = podcastDao.getPodcasts();
                break;
            case PODCAST_ITEM:
                retCursor = podcastDao.getPodcast(ContentUris.parseId(uri));
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
```
