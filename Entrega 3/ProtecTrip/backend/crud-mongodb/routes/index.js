const express = require('express');
const router = express.Router();
const { ObjectId } = require('mongodb');
const { connectToDatabase, getDatabase } = require('../config/database');

/* GET home page. */
router.get('/', async function(req, res, next) {
  try {
    const { database } = await connectToDatabase();
    const collection = database.collection('users');
    const items = await collection.find({}).toArray();
    
    res.render('index', { 
      title: 'Gerenciamento de Usuários', 
      items: items,
      message: req.query.message || null,
      messageClass: req.query.status || ''
    });
  } catch (error) {
    console.error('Erro ao conectar ao banco de dados:', error);
    res.render('index', { 
      title: 'Gerenciamento de Usuários', 
      items: [],
      message: 'Erro ao conectar ao banco de dados',
      messageClass: 'error'
    });
  }
});

/* POST add new item */
router.post('/add', async function(req, res, next) {
  try {
    const { database } = await connectToDatabase();
    const collection = database.collection('users');
    
    const newItem = {
      name: req.body.name,
      username: req.body.username,
      cpf: req.body.cpf,
      phone: req.body.phone,
      password: req.body.password,
      description: req.body.description,
      createdAt: new Date()
    };
    
    await collection.insertOne(newItem);
    res.redirect('/?message=Usuário adicionado com sucesso&status=success');
  } catch (error) {
    console.error('Erro ao adicionar usuário:', error);
    res.redirect('/?message=Erro ao adicionar usuário&status=error');
  }
});

/* GET edit item page */
router.get('/edit/:id', async function(req, res, next) {
  try {
    const { database } = await connectToDatabase();
    const collection = database.collection('users');
    const item = await collection.findOne({ _id: new ObjectId(req.params.id) });
    
    if (!item) {
      return res.redirect('/?message=Usuário não encontrado&status=error');
    }
    
    res.render('edit', { 
      title: 'Editar Usuário',
      item: item,
      message: null,
      messageClass: ''
    });
  } catch (error) {
    console.error('Erro ao buscar usuário para edição:', error);
    res.redirect('/?message=Erro ao buscar usuário&status=error');
  }
});

/* POST update item */
router.post('/update/:id', async function(req, res, next) {
  try {
    const { database } = await connectToDatabase();
    const collection = database.collection('users');
    
    await collection.updateOne(
      { _id: new ObjectId(req.params.id) },
      { 
        $set: {
          name: req.body.name,
          username: req.body.username,
          cpf: req.body.cpf,
          phone: req.body.phone,
          password: req.body.password,
          description: req.body.description,
          updatedAt: new Date()
        }
      }
    );
    
    res.redirect('/?message=Usuário atualizado com sucesso&status=success');
  } catch (error) {
    console.error('Erro ao atualizar usuário:', error);
    res.redirect('/?message=Erro ao atualizar usuário&status=error');
  }
});

/* POST delete item */
router.post('/delete/:id', async function(req, res, next) {
  try {
    const { database } = await connectToDatabase();
    const collection = database.collection('users');
    
    await collection.deleteOne({ _id: new ObjectId(req.params.id) });
    res.redirect('/?message=Usuário excluído com sucesso&status=success');
  } catch (error) {
    console.error('Erro ao excluir usuário:', error);
    res.redirect('/?message=Erro ao excluir usuário&status=error');
  }
});

module.exports = router;
