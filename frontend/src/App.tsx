import { useState } from 'react'
import NavBar from './components/NavBar'
import ClubMain from './views/ClubMain'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <NavBar/>
      <ClubMain/>
    </>
  )
}

export default App
