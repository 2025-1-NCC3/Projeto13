# Instruções para Integração do MongoDB com o Projeto

## Visão Geral

Este projeto implementa um sistema CRUD (Create, Read, Update, Delete) para gerenciamento de usuários, com os seguintes campos:
- Nome
- Usuário
- CPF
- Telefone
- Senha
- Descrição

## Problema de Conexão

Durante a tentativa de conexão com o MongoDB Atlas no ambiente de desenvolvimento atual, encontramos um erro SSL/TLS persistente:

```
SSL routines:ssl3_read_bytes:tlsv1 alert internal error
```

Este erro ocorre devido a incompatibilidades entre a versão do TLS usada pelo ambiente atual e a exigida pelo MongoDB Atlas. Apesar de várias tentativas de configuração, incluindo:

1. Adição de opções SSL/TLS específicas
2. Modificação da string de conexão
3. Uso de parâmetros de conexão adicionais

O problema persiste neste ambiente específico.

## Como Usar o Projeto no Android Studio

Para usar este projeto no Android Studio com MongoDB, siga estas instruções:

### 1. Configuração do Ambiente

1. Certifique-se de que o Node.js esteja instalado em seu ambiente de desenvolvimento
2. Clone ou extraia o projeto em seu ambiente local
3. Navegue até a pasta do projeto e execute `npm install` para instalar as dependências

### 2. Configuração do MongoDB

O projeto está configurado para usar MongoDB Atlas com as seguintes variáveis de ambiente:

```
MONGO_HOST=mongodb+srv://protecDB:protectripDB@protectrip.zoznmjc.mongodb.net/
MONGO_DATABASE=ProtecTrip
PORT=3000
```

Estas variáveis estão no arquivo `.env` na raiz do projeto.

### 3. Possíveis Soluções para o Erro SSL/TLS

Se você encontrar o mesmo erro de SSL/TLS ao executar o projeto localmente, tente:

1. **Atualizar o Node.js**: Certifique-se de usar uma versão recente do Node.js que suporte TLS 1.2 ou superior
2. **Modificar a String de Conexão**: Adicione parâmetros adicionais à string de conexão:
   ```
   mongodb+srv://protecDB:protectripDB@protectrip.zoznmjc.mongodb.net/?ssl=true&tls=true&tlsInsecure=true
   ```
3. **Verificar Configurações de Rede**: Certifique-se de que seu ambiente não tenha restrições de firewall que bloqueiem conexões SSL/TLS

### 4. Estrutura do Projeto

O projeto foi configurado com:

- **Frontend**: Arquivos EJS para visualização e CSS para estilização
- **Backend**: Express.js com rotas para operações CRUD
- **Banco de Dados**: Configuração para MongoDB Atlas

### 5. Executando o Projeto

1. Navegue até a pasta do projeto
2. Execute `npm start` para iniciar o servidor
3. Acesse `http://localhost:3000` no navegador

## Operações CRUD Implementadas

O projeto implementa as seguintes operações CRUD para gerenciamento de usuários:

1. **Create**: Adicionar novos usuários com nome, usuário, CPF, telefone, senha e descrição
2. **Read**: Visualizar todos os usuários cadastrados na página principal
3. **Update**: Editar usuários existentes através da página de edição
4. **Delete**: Remover usuários através do botão de exclusão

## Arquivos Modificados

Os seguintes arquivos foram modificados ou criados:

1. `.env`: Configurações de conexão com o MongoDB
2. `config/database.js`: Módulo de conexão com o MongoDB
3. `views/index.ejs`: Interface principal com formulário e lista de usuários
4. `views/edit.ejs`: Interface para edição de usuários
5. `routes/index.js`: Rotas para operações CRUD
6. `public/stylesheets/style.css`: Estilos CSS para a interface

## Suporte

Se você encontrar problemas adicionais ao conectar com o MongoDB, verifique:

1. Se as credenciais do MongoDB Atlas estão corretas
2. Se o IP do seu ambiente está na lista de IPs permitidos no MongoDB Atlas
3. Se há restrições de rede que possam estar bloqueando a conexão
