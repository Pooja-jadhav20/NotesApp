import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import App from './App'
import Login from './Login'
import NotesList from './NotesList'
import NoteEditor from './NoteEditor'
import PublicNote from './PublicNote'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />}>
          <Route index element={<NotesList />} />
          <Route path="login" element={<Login />} />
          <Route path="notes/:id" element={<NoteEditor />} />
          <Route path="share/:token" element={<PublicNote />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
)
