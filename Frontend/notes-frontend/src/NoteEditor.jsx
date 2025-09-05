import React, { useEffect, useState, useCallback } from "react";
import { useParams } from "react-router-dom";
import api from "./api";

export default function NoteEditor() {
  const { id } = useParams();
  const [note, setNote] = useState(null);

  // define load function outside useEffect, so it can be reused
  const load = useCallback(async () => {
    try {
      const res = await api.get(`/notes/${id}`);
      setNote(res.data);
    } catch (err) {
      console.error("Failed to load note", err);
    }
  }, [id]);

  useEffect(() => {
    if (id) load();
  }, [id, load]);

  async function save() {
    try {
      await api.put(`/notes/${id}`, {
        title: note.title,
        content: note.content,
        version: note.version,
      });
      alert("Saved");
      load(); // now this works ✅
    } catch (err) {
      alert("Error saving note");
      console.error(err);
    }
  }

  async function share() {
    try {
      const res = await api.post(`/notes/${id}/share`, {
        makePublic: true,
        frontendBase: import.meta.env.VITE_FRONTEND_BASE,
      });
      alert("Share URL: " + res.data.shareUrl);
    } catch (err) {
      alert("Error sharing note");
      console.error(err);
    }
  }

  if (!note) return <div>Loading...</div>;

  return (
    <div>
      <input
        value={note.title || ""}
        onChange={(e) => setNote({ ...note, title: e.target.value })}
      />
      <div>
        <textarea
          rows="12"
          cols="60"
          value={note.content || ""}
          onChange={(e) => setNote({ ...note, content: e.target.value })}
        />
      </div>
      <button onClick={save}>Save</button>
      <button onClick={share}>Share</button>
    </div>
  );
}
