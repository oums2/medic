// Récupère l'utilisateur connecté, redirige vers la connexion si absent
function getUtilisateur() {
    const utilisateur = JSON.parse(localStorage.getItem('utilisateur'));
    if (!utilisateur) {
        window.location.href = '/';
        return null;
    }
    return utilisateur;
}

// Page réservée aux patients : redirige admins et médecins
function verifierPatient(utilisateur) {
    if (utilisateur.admin) window.location.href = '/accueilA.html';
    else if (utilisateur.specialite !== undefined) window.location.href = '/accueilM.html';
}

// Page réservée aux médecins : redirige admins et patients
function verifierMedecin(utilisateur) {
    if (utilisateur.admin) window.location.href = '/accueilA.html';
    else if (utilisateur.specialite === undefined) window.location.href = '/accueil.html';
}

// Page réservée aux admins : redirige tous les autres
function verifierAdmin(utilisateur) {
    if (!utilisateur.admin) window.location.href = '/';
}

// Déconnexion : vide le localStorage et redirige vers la connexion
function deconnexion() {
    localStorage.clear();
    window.location.href = '/';
}
