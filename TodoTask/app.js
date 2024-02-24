// Import required modules
const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql2');

// Create Express app
const app = express();
const port = process.env.PORT || 3000;

// Use bodyParser middleware for parsing JSON in requests
app.use(bodyParser.json());

// MySQL Connection
const db = mysql.createConnection({
   host: '127.0.0.1',
   user: 'root',
   password: 'shilo765',
   database: 'sys',
   port: 3306,
   insecureAuth: true,
});

// Connect to MySQL
db.connect(err => {
   if (err) {
      console.error('MySQL connection failed: ' + err.stack);
      return;
   }
   console.log('Connected to MySQL as id ' + db.threadId);
});

// Routes

// GET request to retrieve items for a specific userId
app.get('/items', (req, res) => {
   const userId = req.query.userId;

   if (!userId) {
      return res.status(400).json({ error: 'Missing userId in query parameters' });
   }

   const sql = 'SELECT * FROM todo WHERE userId = ?';

   db.query(sql, [userId], (err, results) => {
      if (err) {
         console.error(err);
         return res.status(500).json({ error: 'Internal Server Error' });
      }

      res.json(results);
   });
});

// POST request to add a new todo
app.post('/api/todos', (req, res) => {
   const { key, cord, date, userId } = req.body;

   if (!key || !cord || !date) {
      return res.status(400).json({ error: 'Please provide key, cord, and date in the request body' });
   }

   const insertQuery = 'INSERT INTO todo (cord, date, userId) VALUES (?, ?, ?)';
   db.query(insertQuery, [cord, date, userId], (err, results) => {
      if (err) throw err;

      res.json({ message: 'Todo added successfully', todoId: results.insertId });
   });
});

// PUT request to update a todo
app.put('/api/todos/:id', (req, res) => {
   const todoId = req.params.id;
   const { cord, date, oldCord, oldDate } = req.body;

   if (!cord || !date) {
      return res.status(400).json({ error: 'Please provide cord and date in the request body' });
   }

   const updateQuery = 'UPDATE todo SET cord = ?, date = ? WHERE userId = ? AND cord = ? AND date = ? LIMIT 1;';
   db.query(updateQuery, [cord, date, todoId, oldCord, oldDate], (err, results) => {
      if (err) throw err;

      if (results.affectedRows === 0) {
         return res.status(404).json({ error: 'Todo not found' });
      }

      res.json({ message: 'Todo updated successfully' });
   });
});

// DELETE request to delete a todo
app.delete('/api/todos/:id,:cord,:date', (req, res) => {
   const todoId = req.params.id;
   const cord = req.params.cord;
   const date = req.params.date;

   if (!todoId) {
      return res.status(400).json({ error: 'Please provide todoId in the request parameters' });
   }

   const deleteQuery = 'DELETE FROM todo WHERE userId = ? AND cord = ? AND date = ?';
   db.query(deleteQuery, [todoId, cord, date], (err, results) => {
      if (err) throw err;

      if (results.affectedRows === 0) {
         return res.status(404).json({ error: 'Todo not found' });
      }

      res.json({ message: 'Todo deleted successfully' });
   });
});

// Start the server
app.listen(port, () => {
   console.log(`Server is running on port ${port}`);
});

// Export the Express app
module.exports = app;
