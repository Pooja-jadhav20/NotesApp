import React, {useEffect, useState} from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

export default function PublicNote(){
  const { token } = useParams();
  const [note, setNote] = useState(null);
  useEffect(() => {
    async function fetchNote() {
      const res = await axios.get(`${import.meta.env.VITE_API_URL.replace('/api','')}/api/share/${token}`);
      setNote(res.data);
    }
    fetchNote();
  }, [token]);
  if (!note) return <div>Not found</div>;
  return (
    <div>
      <h2>{note.title}</h2>
      <div>{note.content}</div>
    </div>
  )
}
