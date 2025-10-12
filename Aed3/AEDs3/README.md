Como compilar e executar

Requisito: JDK 17+ instalado.

1.Limpar e compilar

rm -rf bin dados/*
mkdir -p bin
javac -cp lib/jnanoid-2.0.0.jar -d bin $(find . -name "*.java")

2.Executar

java -cp "bin:lib/jnanoid-2.0.0.jar" App


PresenteFácil 1.0 — TP1 (Relacionamento 1:N)

Sistema em linha de comando para gestão de listas de presentes.
Cada Usuário pode criar N Listas, e cada Lista possui um código compartilhável (NanoID) para consulta por terceiros.
Este repositório implementa o TP1: usuários + listas (sem produtos ainda).

Arquitetura: Java + Arquivos binários + Índices

CRUD genérico (arquivo de dados com lápide + tamanho + bytes)

Índice direto: Hash Extensível (id → endereço)

Índice por e-mail: Hash Extensível (email → id)

Relação 1:N (Usuário→Listas): Árvore B+ com chaves (idUsuario; idLista)

MVC (packages: model, dao, controller, view)

NanoID para código compartilhável de lista



Licença

Projeto acadêmico — uso livre para fins educacionais.
