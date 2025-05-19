const { MongoClient } = require('mongodb');
require('dotenv').config();

// Teste de conexão com o MongoDB
async function testConnection() {
  const uri = process.env.MONGO_HOST;
  const dbName = process.env.MONGO_DATABASE;
  
  console.log('Tentando conectar ao MongoDB com opções SSL/TLS ajustadas...');
  console.log(`URI: ${uri}`);
  console.log(`Database: ${dbName}`);
  
  try {
    // Adicionando opções de conexão para resolver problemas de SSL/TLS
    const options = {
      ssl: true,
      tls: true,
      tlsAllowInvalidCertificates: true
    };
    
    // Modificando a string de conexão para incluir parâmetros adicionais
    let connectionString = uri;
    if (!connectionString.includes('?')) {
      connectionString += '?';
    } else {
      connectionString += '&';
    }
    connectionString += 'ssl=true&tls=true&tlsInsecure=true&retryWrites=true&w=majority';
    
    const client = new MongoClient(connectionString, options);
    await client.connect();
    console.log('Conexão com MongoDB estabelecida com sucesso!');
    
    const database = client.db(dbName);
    console.log('Banco de dados acessado com sucesso!');
    
    // Verificar se a coleção 'users' existe, se não, criar
    const collections = await database.listCollections({name: 'users'}).toArray();
    if (collections.length === 0) {
      console.log('Criando coleção "users"...');
      await database.createCollection('users');
      console.log('Coleção "users" criada com sucesso!');
    } else {
      console.log('Coleção "users" já existe.');
    }
    
    // Listar todas as coleções
    const allCollections = await database.listCollections().toArray();
    console.log('Coleções disponíveis:');
    allCollections.forEach(collection => {
      console.log(` - ${collection.name}`);
    });
    
    await client.close();
    console.log('Conexão fechada.');
    return true;
  } catch (error) {
    console.error('Erro ao conectar ao MongoDB:', error);
    return false;
  }
}

testConnection()
  .then(success => {
    if (success) {
      console.log('Teste de conexão concluído com sucesso!');
    } else {
      console.log('Teste de conexão falhou!');
    }
  })
  .catch(console.error);
