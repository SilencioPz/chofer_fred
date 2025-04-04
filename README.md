🚘 ChoferFred - Aplicativo de Viagens Familiares (Spring Boot) 🚘
<p align="center"> <img src="https://img.icons8.com/fluency/96/000000/car.png" width="100"> <br> <em>Aplicativo de viagens municipais para parentes e amigos.</em>🚘</p>
----------------------------------------------------------------------------------------------------------------------------------------------------------

✨ Sobre o Projeto

Aplicativo desktop desenvolvido em Spring Boot que permite:

    ✅ Marcar corridas com um familiar de confiança.

    🎨 Interface simples e amistosa, graças ao Bootstrap.

    🚫 Não utiliza Google Maps em tempo real, apenas Routes API.
----------------------------------------------------------------------------------------------------------------------------------------------------------
  🚀 Roadmap
--------------------------------------------------------------------------
Versão	               |   Status  |	        Observação               |
--------------------------------------------------------------------------
Spring Boot	           |✅ Completo|	Versão estável (IntelliJ + Maven)|
--------------------------------------------------------------------------
Android                |🔄 Em breve|	Versao mobile usara Kotlin       |
----------------------------------------------------------------------------------------------------------------------------------------------------------
🛠️ Tecnologias & Ferramentas
----------------------------------------------------------------------------------------------------------------------------------------------------------
Componente	  Detalhes
----------------------------------------------------------------------------------------------------------------------------------------------------------
Linguagem	    Java 17 (OpenJDK)
----------------------------------------------------------------------------------------------------------------------------------------------------------
IDE	          IntelliJ IDEA
----------------------------------------------------------------------------------------------------------------------------------------------------------
Biblioteca	  Spring Boot Starter Parent 3.2.0
----------------------------------------------------------------------------------------------------------------------------------------------------------
Build Tool	  Maven
----------------------------------------------------------------------------------------------------------------------------------------------------------
📂 Estrutura do Projeto
----------------------------------------------------------------------------------------------------------------------------------------------------------
ChoferFred/
<br>
├── src/
<br>
│  ├── main/java/org/example/
<br>
│   │   ── Main             # Arquivo Principal
<br>
│   │   -─ SimpleStorage/   # Utiliza Map(não-utilizado)
<br>
│   │   ── UserSession/     # Definição de Usuário(não-utilizado)
<br>
|   |---config              # Arquivos de configuração
<br>
|   |---controller          # Arquivos de definição de Rotas
<br>
|   |---model               # Arquivos responsáveis pelo agendamento de corridas
<br>
|   |---service             # Arquivos de Configuração para as APIs utilizadas (Google Calendar, Google Maps, Telegram Bot)
<br>
│   ── test/                # Testes unitários(não-utilizado)
<br>
── target/                  # Builds (ignorado pelo Git)
<br>
── ChoferFred-1.0-SNAPSHOT.jar    # Executável final
<br>
----------------------------------------------------------------------------------------------------------------------------------------------------------
⚡ Como Executar
<br>
Pré-requisitos

    Java 17+ instalado (java -version).

----------------------------------------------------------------------------------------------------------------------------------------------------------
Métodos
1. Via JAR:
   java -jar ChoferFred-1.0-SNAPSHOT.jar
   
3. No IntelliJ:

    Importe como projeto Maven.

    Execute Main.java (classe principal).

----------------------------------------------------------------------------------------------------------------------------------------------------------
   🔒 Boas Práticas

✔️ .gitignore otimizado para Java/Maven.
<br>
✔️ Arquitetura MVC (Model-View-Controller).
<br>
✔️ Tratamento de exceções para arquivos inválidos.
<br>
✔️ Documentação no código (JavaDoc).
<br>
----------------------------------------------------------------------------------------------------------------------------------------------------------
🌟 Próximos Passos

    🔗 Portabilidade para celulares Android utilizando Kotlin como Linguagem de Programação.

    🖨️ Mapas em Tempo Real, ampliando a satisfação do usuário.

    🐧 Responsividade para diferentes tamanhos de telas.
----------------------------------------------------------------------------------------------------------------------------------------------------------
<p align="center"> 👨‍💻 Desenvolvido com ☕ e ♫! <br> <em>Feito no 🇧🇷 com muita zuera!!!</em> 😄😎 </p>
