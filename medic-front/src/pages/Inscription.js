import { useState } from "react";
import "./Inscription.css";

const champsCommuns = [
  { name: "nom",        label: "Nom",            type: "text" },
  { name: "prenom",     label: "Prénom",          type: "text" },
  { name: "email",      label: "Email",           type: "email" },
  { name: "motDePasse", label: "Mot de passe",    type: "password" },
];

const champsPatient = [
  { name: "adresse", label: "Adresse", type: "text" },
  { name: "numero",  label: "Téléphone", type: "tel" },
];

const champsMedecin = [
  { name: "specialite", label: "Spécialité", type: "text" },
];

export default function Inscription() {
  const [role, setRole] = useState("patient");
  const [form, setForm] = useState({});
  const [message, setMessage] = useState(null);
  const [erreur, setErreur] = useState(null);
  const [chargement, setChargement] = useState(false);

  const champsSpecifiques = role === "patient" ? champsPatient : champsMedecin;
  const endpoint =
    role === "patient"
      ? "http://localhost:8080/patients/inscription"
      : "http://localhost:8080/medecins/inscription";

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setChargement(true);
    setMessage(null);
    setErreur(null);

    try {
      const res = await fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      if (res.ok) {
        const data = await res.json();
        setMessage(`Inscription réussie ! Bienvenue ${data.prenom} ${data.nom}.`);
        setForm({});
        e.target.reset();
      } else {
        setErreur("Erreur lors de l'inscription. Vérifiez vos informations.");
      }
    } catch {
      setErreur("Impossible de contacter le serveur.");
    } finally {
      setChargement(false);
    }
  }

  return (
    <div className="inscription-container">
      <div className="inscription-card">
        <h1>Inscription</h1>

        {/* Choix du rôle */}
        <div className="role-toggle">
          <button
            type="button"
            className={role === "patient" ? "actif" : ""}
            onClick={() => { setRole("patient"); setForm({}); }}
          >
            Je suis patient
          </button>
          <button
            type="button"
            className={role === "medecin" ? "actif" : ""}
            onClick={() => { setRole("medecin"); setForm({}); }}
          >
            Je suis médecin
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          {[...champsCommuns, ...champsSpecifiques].map((champ) => (
            <div className="champ" key={champ.name}>
              <label htmlFor={champ.name}>{champ.label}</label>
              <input
                id={champ.name}
                name={champ.name}
                type={champ.type}
                value={form[champ.name] || ""}
                onChange={handleChange}
                required
                placeholder={champ.label}
              />
            </div>
          ))}

          <button type="submit" className="btn-submit" disabled={chargement}>
            {chargement ? "Inscription en cours..." : "S'inscrire"}
          </button>
        </form>

        {message && <p className="succes">{message}</p>}
        {erreur   && <p className="erreur">{erreur}</p>}
      </div>
    </div>
  );
}
