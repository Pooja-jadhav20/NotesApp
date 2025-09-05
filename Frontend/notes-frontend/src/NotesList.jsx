import React, {useEffect, useState} from "react";
import api from "./api";
import { Link } from "react-router-dom";

export default function NotesList(){
  const [notes,setNotes] = useState([]);
  useEffect(()=>{ fetchNotes(); },[]);
  async function fetchNotes(){
    try {
      const token = localStorage.getItem("token");
      if (token) api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      const res = await api.get("/notes");
      setNotes(res.data.content || res.data.items || res.data);
    } catch(e){
      console.error(e);
      setNotes([]);
    }
  }

  async function createBlank(){
    await api.post("/notes", { title: "New note", content: "" });
    fetchNotes();
  }

  return (
    <div>
      <button onClick={createBlank}>Create Note</button>
      <ul>
        {notes.map(n => (
          <li key={n.id}>
            <Link to={`/notes/${n.id}`}>{n.title || "Untitled"}</Link>
          </li>
        ))}
      </ul>
    </div>
  )
}
