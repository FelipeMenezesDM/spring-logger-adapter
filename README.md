# Spring Logger Adapter
Biblioteca para gerenciamento de eventos de log para aplicações desenvolvidas com Spring Boot.

## Tópicos
- [Instalação com Maven](#instalação-com-maven)
- [Deploy manual](#deploy-manual)
- [Uso](#uso)
- [Configuração](#configuração)

## Instalação com Maven
Crie o arquivo de configuração do maven ou inclua o repositório e o servidor no arquivo já existente:
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" 
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <activeProfiles>
    <activeProfile>general</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>general</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo1.maven.org/maven2</url>
        </repository>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/felipemenezesdm/spring-logger-adapter</url>
        </repository>
      </repositories>
    </profile>
  </profiles>
</settings>
```

Inclua a dependência no arquivo pom:
```xml
<dependency>
  <groupId>br.com.felipemenezesdm</groupId>
  <artifactId>spring-logger-adapter</artifactId>
  <version>1.0.0</version>
</dependency>
```

Execute com comando abaixo para download de dependências:
```
mvn install
```

## Deploy manual
O deploy da biblioteca é realizado automaticamente sempre que houver a criação de uma nova tag de versão. Entretanto, se for necessário realizar seu deploy manual, é preciso seguir os passos abaixo:

1. No _settings.xml_, confirmar que o servidor do GitHub está configurado:
    ```xml
      <servers>
        <server>
          <id>github</id>
          <username>${repo.usrnm}</username>
          <password>${repo.pswd}</password>
        </server>
      </servers>
    ```
2. Executar o comando abaixo, substuindo os parâmetros por seus respectivos valores:
    ```
    mvn deploy -s settings.xml -Drepo.usrnm=USERNAME -Drepo.pswd=PASSWORD
    ```

## Uso
Para usar esta biblioteca, basta importar a classe _LogHandler_ e utilizar seus métodos estáticos para definir as fronteiras de log:

```java
import br.com.felipemenezesdm.LogHandler;

public class Application { 
    public static void main(String[] args) {
        LogHandler.info("Message");
        // LogHandler.waning("Message");
        // LogHandler.error("Message");
    }
}
```

O _LogPayload_ pode ser utilizado para a criação de um payload padronizado para os eventos de log. Ele utiliza o _builder pattern_, e pode ser implementado da seguinte forma:
```java
import br.com.felipemenezesdm.LogHandler;
import br.com.felipemenezesdm.LogPayload;

public class Application {
    public static void main(String[] args) {
        LogHandler.info("Message", LogPayload.build()
                .setMessage("Message")
                .setCorrelationId("ID")
        );
    }
}
```

Para o _LogPayload_, não é necessário definir os atributos "severity", "service ID" e "duration". Por padrão, o atributo "severity" será preenchido a partir do tipo de log iniciado ("info", "warning" ou "error"). O atributo "service ID", por sua vez, pode ser informado no payload, porém será preenchido utilizando a variável de ambiente "APP_SERVICE_ID", quando for nulo. Já o atributo "duration" pode ser definido da seguinte forma:
```java
import br.com.felipemenezesdm.LogHandler;
import br.com.felipemenezesdm.LogPayload;

public class Application { 
    public static void main(String[] args) {
        LogHandler.registerLogger("test");              // Inicialização de novos  atributos de log
        LogHandler.info("Message", LogPayload.build()
                .setLoggerId("test")                    // Vinculação do payload aos atributos inicializados
                .setMessage("Message")
                .setCorrelationId("ID")
        );
    }
}
```

O método _registerLogger(String loggerId)_ do _LogHandler_ irá inicializar todos od parâmetros individualizados de uma fronteira de log, identificados por um ID único.

O método _setLoggerId(String loggerId)_ do _LogPayload_ irá vincular o payload aos atributos inicializados. 

No caso do atributo "duration", é necessário se atentar para o posicionamento do _registerLogger_, já que ele também inicializará o contador que será utilizado para calcular a duração de execução do trecho de código.

Um exemplo mais prático seria o cálculo de duração de uma consulta na base, que poderia ser feita desta forma:

```java
import br.com.felipemenezesdm.LogHandler;
import br.com.felipemenezesdm.LogPayload;

public class Application { 
    public static void main(String[] args) {
        LogHandler.registerLogger("test");              // Início do contador
        repository.getAllUsers();                       // Execução da consulta
        LogHandler.info("Message", LogPayload.build()
                .setLoggerId("test")                    // Fim do contador
                .setMessage("Message")
                .setCorrelationId("ID")
        );
    }
}
```

## Configuração
Esta biblioteca não utiliza bibliotecas externas para formatação de logs, ou seja, é possível utilizar as configurações que o próprio Spring Boot disponibiliza.

Por padrão, o formato das mensagens de log é:
```
"%d{[yyyy-MM-dd HH:mm:ss]} ${spring.profiles.active:default}.%p: %m%n"
```

Para alterar este formato, basta usar a propriedade _logging.pattern.console_ no _application.yml_:
```yaml
logging:
  pattern:
    console: "%d{[yyyy-MM-dd HH:mm:ss]} ${spring.profiles.active:default}.%p: %m%n"
```
