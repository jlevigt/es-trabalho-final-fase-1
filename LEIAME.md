# Kit da fase 1 (caixa-preta)

- `lib/biblioteca-1.0.jar`: o sistema da biblioteca, compilado. Ele contém defeitos.
- `src/test/java`: escrevam aqui os testes (JUnit 5). Há um exemplo em `ExemploTest.java`.
- A especificação do sistema está no Anexo A do enunciado.

Para rodar: abram a pasta como projeto Maven (IntelliJ, VS Code ou Eclipse) ou usem `mvn test`.
Requer JDK 17 ou superior.

Testes que falham são esperados: o resultado esperado vem da especificação, então uma falha
indica um possível defeito. Nunca ajustem o valor esperado para o que o sistema devolve.

Façam um repositório git desta pasta desde já: o histórico será usado na fase 3.
