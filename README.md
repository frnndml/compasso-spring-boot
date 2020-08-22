# compasso-spring-boot

## Microserviço para cadastro de Clientes
Endpoints disponíveis:
* Cadastrar cidade
* Cadastrar cliente
* Consultar cidade pelo nome
* Consultar cidade pelo estado
* Consultar cliente pelo nome
* Consultar cliente pelo Id
* Remover cliente
* Alterar o nome do cliente

Entidades:
* Cidades: nome e estado
* Cliente: nome completo, sexo, data de nascimento, idade e cidade onde mora

## Executando a aplicação
- Use ```mvn package``` para gerar o arquivo executável.
- Para executar a aplicação juntamente com o banco de dados, utilize ```docker-compose up --build```.
- Acesse a aplicação em <code>http://localhost:8080/</code>
- Acesse a documentação da API em <code>http://localhost:8080/swagger-ui.html</code>
