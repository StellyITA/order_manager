import './App.css';
import MenuItem from './components/MenuItem';
import { useState, useEffect } from 'react';
import axios from 'axios';

function App() {

  const [data, setData] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/menu")
         .then((response) => {
          setData(response.data);
         })
         .catch(err => {
          console.log(err);
         })
  });

  return (
    <div className="App">
      <header className="App-header">
        Header
      </header>
      <div className='display-menu'>
      {data.map(el => {
          return (
            <MenuItem
              name={el["dish_name"]}
              price={el["price"]}
              key={el["dish_id"]}
            />
          )
        })}
      </div>
    </div>
  );
}

export default App;
