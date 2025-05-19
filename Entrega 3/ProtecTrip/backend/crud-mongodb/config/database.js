const { MongoClient } = require('mongodb');
require('dotenv').config();

const uri = process.env.MONGO_HOST;
const dbName = process.env.MONGO_DATABASE;

let client;
let database;

async function connectToDatabase() {
  try {
    if (!client) {
      // Modificando a string de conexão para incluir parâmetros adicionais
      let connectionString = uri;
      
      // Verificar se a URI já contém parâmetros de consulta
      if (!connectionString.includes('?')) {
        connectionString += '?';
      } else {
        connectionString += '&';
      }
      
      // Adicionar parâmetros para lidar com problemas de SSL/TLS
      connectionString += 'ssl=true&tls=true&tlsInsecure=true&retryWrites=true&w=majority';
      
      client = new MongoClient(connectionString);
      await client.connect();
      console.log('Conectado ao servidor MongoDB');
    }
    
    database = client.db(dbName);
    return { client, database };
  } catch (error) {
    console.error('Erro ao conectar ao MongoDB:', error);
    throw error;
  }
}

function getDatabase() {
  if (!database) {
    throw new Error('Banco de dados não está conectado. Chame connectToDatabase() primeiro.');
  }
  return database;
}

function getClient() {
  if (!client) {
    throw new Error('Cliente MongoDB não está conectado. Chame connectToDatabase() primeiro.');
  }
  return client;
}

module.exports = {
  connectToDatabase,
  getDatabase,
  getClient
};
