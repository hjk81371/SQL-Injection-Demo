const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const port = 5000;

// Enable CORS for React frontend
app.use(cors());

// Use body parser to handle POST request data
app.use(bodyParser.json());

// Connect to MySQL database (Make sure MySQL is running locally)
const db = mysql.createConnection({
    host: 'database-injection.c1qqsy6ecrz8.us-east-2.rds.amazonaws.com',
    user: 'admin',
    password: 'scsHackHour2024', // Add your password here if necessary
    database: 'main_db'
});

// Make sure the DB is connected
db.connect((err) => {
    if (err) {
        console.error('Error connecting to MySQL:', err);
    } else {
        console.log('Connected to MySQL!');
    }
});

// Vulnerable login endpoint
app.post('/login', (req, res) => {
    const { username, password } = req.body;

    // Vulnerable query that is susceptible to SQL injection
    const query = `SELECT * FROM users WHERE username = '${username}' AND password = '${password}'`;
    console.log("QUERY: ", query);

    db.query(query, (err, results) => {
        if (err) {
            res.status(500).send('Error with database query');
        } else if (results.length > 0) {
            res.status(200).send('Login successful');
        } else {
            res.status(401).send('Invalid credentials');
        }
    });
});



// Endpoint to demonstrate SQL injection vulnerability by dumping users (for demo only)
app.post('/login2', (req, res) => {
    const { username, password } = req.body;
    // Vulnerable query to select all users
    const query = `SELECT username, password FROM users WHERE username = '${username}' AND password = '${password}'`;
    console.log("QUERY: ", query);

    db.query(query, (err, results) => {
        if (err) {
            res.status(500).send('Error with database query');
        } else {
            // Return list of usernames and passwords
            res.status(200).json(results);
        }
    });
});



app.get('/hello', (req, res) => {
    res.send('Hello, world!');
});


app.listen(port, () => {
    console.log(`Server running on http://localhost:${port}`);
});
