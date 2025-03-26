// Importando os pacotes necessários para o servidor
const express = require('express'); // Framework para criar o servidor HTTP
const sql = require('mssql'); // Biblioteca para conectar ao banco de dados SQL Server
const cors = require('cors'); // Middleware para permitir requisições de diferentes origens (CORS)
const bodyParser = require('body-parser'); // Middleware para processar JSON no corpo das requisições
const bcrypt = require('bcrypt'); // Biblioteca para criptografar senhas
const jwt = require('jsonwebtoken'); // Biblioteca para gerar tokens de autenticação
require('dotenv').config(); // Carrega variáveis de ambiente do arquivo .env

// Criando a aplicação Express
const app = express();
const PORT = process.env.PORT || 3000; // Define a porta do servidor, padrão 3000 caso não esteja definida no .env

// Configuração de Middlewares
app.use(cors()); // Habilita o CORS para permitir requisições do frontend
app.use(bodyParser.json());  // Configura o servidor para aceitar JSON no corpo das requisições

// Configuração do banco de dados SQL Server no Azure
const config = {
    user: 'GabrielSilva',           // Usuário do banco de dados
    password: 'Kiansabe1',         // Senha do banco de dados
    server: 'databaseuberapp.database.windows.net',  // Nome do servidor no Azure
    database: 'DadosUberApp',     // Nome do banco de dados
    options: {
        encrypt: true,             // Necessário para conexões com o Azure
        trustServerCertificate: true  // Para evitar erros de certificado SSL
    }
};

// Função para conectar ao banco de dados
async function connectToDatabase() {
    try {
        await sql.connect(config); // Tenta estabelecer a conexão
        console.log('Conectado ao banco de dados SQL do Azure');
    } catch (err) {
        console.error('Erro ao conectar ao banco de dados:', err); // Loga erros de conexão
    }
}
connectToDatabase(); // Chama a função para conectar ao banco

// Rota de teste para verificar se a API está funcionando
app.get('/api/teste', (req, res) => {
    res.status(200).json({ message: 'API funcionando!' });
});

// // Inicializando o servidor na porta definida
app.listen(PORT, () => {
    console.log(`Servidor rodando na porta ${PORT}`)
});


// Rota para cadastrar um novo usuário

app.post('/register', async(req, res) => {
    const{ nome, email, senha } = req.body; // Extrai os dados do corpo da requisição

    // Verifica se todos os campos foram preenchidos
    if (!nome || !email || !senha){
        return res.status(400).json({message: "Preencha todos os campos"});
    } 

    
    try {
        // Verifica se o email já está cadastrado
        const result = await sql.query`SELECT * FROM users WHERE email = ${email}`;
        
        if (result.recordset.length > 0) {
            return res.status(400).json({ message: "Email já cadastrado!" });
        }


        // Criptografa a senha antes de armazenar no banco
        const hashedPassword = await bcrypt.hash(senha, 10);

        // Insere o novo usuário no banco de dados
        await sql.query`INSERT INTO users (nome, email, senha) VALUES (${nome}, ${email}, ${hashedPassword})`;

        res.status(201).json({ message: 'Cadastro realizado com sucesso!' });
    } catch (err) {
        console.error('Erro ao cadastrar usuário:', err.message, err.stack);
        res.status(500).json({ message: 'Erro ao cadastrar usuário!' });
    }
});
    

// Rota para autenticar um usuário
app.post('/login', async (req, res) => {
    const { email, senha } = req.body; // Extrai email e senha da requisição

    // Verifica se os campos foram preenchidos
    if(!email || !senha) {
        return res.status(400).json({ message: 'Preencha todos os campos!'});
    }

    try {
        // Busca o usuário no banco pelo email
        const result = await sql.query`SELECT * FROM users WHERE email = ${email}`;

        if (result.recordset.length === 0) {
            return res.status(401).json({ message: 'Usuário não encontrado!' });
        }

        const user = result.recordset[0]; // Obtém os dados do usuário

        // Verifica se a senha fornecida corresponde à senha criptografada no banco
        const isMatch = await bcrypt.compare(senha, user.senha);
        if (!isMatch) {
            return res.status(401).json({ message: 'Senha incorreta!' });
        }

        // Gera um token JWT para autenticação
        const token = jwt.sign({ id: user.id }, 'segredo', { expiresIn: '1h' });

        res.status(200).json({ message: 'Login bem-sucedido!', token });
    } catch (err) {
        console.error('Erro ao autenticar usuário:', err);
        res.status(500).json({ message: 'Erro ao autenticar usuário!' });
    }
});