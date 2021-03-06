package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BostjanSkok on 6.4.2014.
 */
public class Patricia<T> {

    //Ustvarimo korensko vozlisce
    private PatriciaNode<T> root = new PatriciaNode<T>(false, "", null);

    public void Vstavi(String key, T data) throws Exception {
        Vstavi(this.root, key, data);
    }

    public T Najdi(String key) {
        return find(root, key);
    }

    private T find(PatriciaNode<T> node, String key) {
        int found = node.FoundInKey(key);
        if (node.GetKey().equals("") == true || found < key.length() && found < node.GetKey().length()) {
            //Iscemo potomca ki se zcne z prvmi znakom kljuca
            String noviKljuc = key.substring(found, key.length());
            for (PatriciaNode<T> potomec : node.VrniPotomce()) {
                if (potomec.GetKey().startsWith(noviKljuc.charAt(0) + "")) {
                    return find(potomec, noviKljuc);
                }
            }
            return null;
        } else if (found == key.length() && node.GetKey().length() == found) {
            if (node.isLeaf)
                return node.data;
            return null;
        }
        return null;
    }

    private void Vstavi(PatriciaNode<T> node, String key, T data) throws Exception {

        int zadetki = node.FoundInKey(key);

        //Primer ko samo dodamo novo vozlice ali smo v korenu ali so zadetki manjsi od klujca v vozliscu
        //in kljuc ki ga ustavljamo je vecji od zadetkov
        String nodeKey = node.GetKey();
        if (nodeKey.equals("") == true || zadetki == 0 || (zadetki < key.length() && zadetki >= nodeKey.length())) {
            boolean njadenPotomec = false;
            //Odstranimo del kljuca ki je ze notr
            String noviKljuc = key.substring(zadetki, key.length());
            //Iscemo potomca ki se zacne z prvo crko ostanka kljuca
            for (PatriciaNode<T> potomec : node.VrniPotomce()) {
                if (potomec.GetKey().startsWith(noviKljuc.charAt(0) + "")) {
                    njadenPotomec = true;
                    Vstavi(potomec, noviKljuc, data);
                    break;
                }
            }

            // ce ni nobenega vstreznega potomca potem ustavimo preostali kljuc kot novoega potomca trenutnega
            if (njadenPotomec == false) {
                PatriciaNode<T> novi = new PatriciaNode<T>(true, noviKljuc, data);
                node.DodajPotomca(novi);
            }

        } else if (zadetki > 0 && zadetki < nodeKey.length()) {
            //Ko je del kljuca najde vozlisce je pa daljse ga je potrebno razdeliti na dva kosa
            node.RazdeliPoKljucu(zadetki);
            PatriciaNode<T> novi = new PatriciaNode<T>(true, key.substring(zadetki, key.length()), data);
            node.DodajPotomca(novi);

        } else if (zadetki == key.length() && zadetki == nodeKey.length()) {
            //V tem primer smo dobili celoten niz v vozliscu
            node.MakeLeaf(data);

        } else {
            // Prisli smo do konca iskanja moramo dodati novo vozlisce
            PatriciaNode<T> novi = new PatriciaNode<T>(true, key.substring(zadetki, key.length()), data);
            node.DodajPotomca(novi);
        }


    }

    public T Brisi(String key) throws Exception {
        return brisi(key, null, root);
    }

    /**
     *
     * @param key kljuc ki brisemo
     * @param parent stars vozlisca
     * @param node vozlisce
     * @return Vrnemo podatek tipa T ki je shranjen na kljucu ce kljuc ne obstaja null
     * @throws Exception  napaka v primeru da se kljuce zdruzi na vozliscu ki nima tocno enega potomca
     * */
    private T brisi(String key, PatriciaNode<T> parent, PatriciaNode<T> node) throws Exception {

        int found = node.FoundInKey(key);
        if (node.GetKey().equals("") == true || found < key.length() && found < node.GetKey().length()) {
            //Iscemo potomca ki se zcne z prvmi znakom kljuca
            String noviKljuc = key.substring(found, key.length());
            for (PatriciaNode<T> potomec : node.VrniPotomce()) {
                if (potomec.GetKey().startsWith(noviKljuc.charAt(0) + "")) {
                    return brisi(noviKljuc, node, potomec);
                }
            }
            return null;
        } else if (found == key.length() && node.GetKey().length() == found) {
            if (node.isLeaf) {
                //Nasli smo list za izbrisati
                if (node.NumberOfChildren() == 0) {
                    //list odstranimo
                    parent.OdstraniPotomca(node);
                    if (parent.NumberOfChildren() == 1 && !parent.isLeaf && !parent.GetKey().equals("")) {
                        //Ce ima stars samo se enega potomca ki ni list ne koren patricije jih zdruzimo
                        parent.Zdruzi();
                    }
                }
                return node.data;
            }

            return null;
        }
        return null;
    }

    private class PatriciaNode<T> {

        private boolean isLeaf; // pove ce je vozlisce konce kljuca
        private T data; //podatek hranjen
        private String key;

        private List<PatriciaNode<T>> children;

        private PatriciaNode(boolean isLeaf, String key, T data) {
            this.isLeaf = isLeaf;
            this.key = key;
            if (isLeaf)
                this.data = data;
            children = new ArrayList<PatriciaNode<T>>();
        }

        public List<PatriciaNode<T>> VrniPotomce() {
            return children;
        }

        public String GetKey() {
            return key;
        }

        public int NumberOfChildren() {
            return children.size();
        }

        private int FoundInKey(String toFind) {
            for (int i = 0; i < key.length(); i++) {
                //ce smo prisli do konca iskanega niza  ali ce se znaki ne ujemajo  vrnemo pozicijo ozirma i
                if (toFind.length() - 1 <= i || key.charAt(i) != toFind.charAt(i)) {
                    return i + 1;
                }
            }
            return 0;
        }


        public void DodajPotomca(PatriciaNode<T> novi) {
            children.add(novi);
        }

        public void RazdeliPoKljucu(int zdetki) {
            PatriciaNode<T> noviOtrok = new PatriciaNode<T>(this.isLeaf, this.key.substring(zdetki, this.key.length()), this.data);
            noviOtrok.children = this.children;
            this.DodajPotomca(noviOtrok);

            this.key = this.key.substring(0, zdetki);
            this.data = null;
            this.isLeaf = false;

        }


        public void MakeLeaf(T data) throws Exception {
            //Preverimo ce je ze list pomeni da je prislo do dvakratnega ustavljanja istega kljuca kar povzroci napako
            if (this.isLeaf)
                throw new Exception("Dvojni kljuc");

            this.isLeaf = true;
            this.data = data;

        }

        public void OdstraniPotomca(PatriciaNode<T> node) {
            children.remove(node);
        }

        public void Zdruzi() throws Exception {
            if (this.children.size() != 1)
                throw new Exception("Zdruzevanje v vozlisca ki nima  enega potomca");

            PatriciaNode<T> node = children.get(0);
            this.key += node.key;
            this.data = node.data;
            this.isLeaf = node.isLeaf;
        }
    }


}

