package no.oslomet.cs.algdat.Oblig3;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;

public class SBinTre<T> {
   private static final class Node<T>   // en indre nodeklasse
   {
      private T verdi;                   // nodens verdi
      private Node<T> venstre, høyre;    // venstre og høyre barn
      private Node<T> forelder;          // forelder

      // konstruktør
      private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
         this.verdi = verdi;
         venstre = v;
         høyre = h;
         this.forelder = forelder;
      }

      private Node(T verdi, Node<T> forelder)  // konstruktør
      {
         this(verdi, null, null, forelder);
      }

      @Override
      public String toString() {
         return "" + verdi;
      }

   } // class Node

   private Node<T> rot;                            // peker til rotnoden
   private int antall;                             // antall noder
   private int endringer;                          // antall endringer

   private final Comparator<? super T> comp;       // komparator

   public SBinTre(Comparator<? super T> c)    // konstruktør
   {
      rot = null;
      antall = 0;
      comp = c;
   }

   public boolean inneholder(T verdi) {
      if (verdi == null) return false;

      Node<T> p = rot;

      while (p != null) {
         int cmp = comp.compare(verdi, p.verdi);
         if (cmp < 0) {
            p = p.venstre;
         } else if (cmp > 0) {
            p = p.høyre;
         } else return true;
      }

      return false;
   }

   public int antall() {
      return antall;
   }

   public String toStringPostOrder() {
      if (tom()) return "[]";

      StringJoiner s = new StringJoiner(", ", "[", "]");

      Node<T> p = førstePostorden(rot); // går til den første i postorden
      while (p != null) {
         s.add(p.verdi.toString());
         p = nestePostorden(p);
      }

      return s.toString();
   }

   public boolean tom() {
      return antall == 0;
   }


   public boolean leggInn(T verdi) {
      Node<T> p = rot;  // Setter peker p på rotnode.
      Node<T> q = null; // Instansierer node q.
      int cmp = 0;      // Instansierer comperator variabel.

      while (p != null) {
         q = p;                              // Tilordner at variabelen q skal være peker p
         cmp = comp.compare(verdi, p.verdi); // Bruker comperator for å sammenlikne verdiene.
         if (cmp < 0) {                      // sammenlikner  cmp resultater om det er -1, 0 eller 1.
            p = p.venstre;                   // Dersom -1, flytt peker p mot venstre
         } else {
            p = p.høyre;                     // Hvis ikke -1 (0 eller 1), flytt til høyre.
         }
      }

      if (q == null) {                            // Tomt tre ? Ja ass I'm an orpha ☹.
         p = new Node<>(verdi, null);     // Oppretter ny node med foreldernode lik null.
         rot = p;
      } else {
         p = new Node<>(verdi, q);     // Oppretter ny node med foreldernode lik null.
         if (cmp < 0) {
            q.venstre = p;
         } else {
            q.høyre = p;
         }
      }

      antall++;                             // Tilordner antall noder i dette treet.
      return true;
   }

   // Importert fra kompendie -> Programkode 5.2.8 d)
   public boolean fjern(T verdi) {
      if (verdi == null) return false;
      Node<T> p = rot;
      Node<T> q = null;       // q er satt til forelder.

      while (p != null) {
         int cmp = comp.compare(verdi, p.verdi);
         if (cmp < 0) {       // går til venstre
            q = p;
            p = p.venstre;
         } else if (cmp > 0) { // går til høyre
            q = p;
            p = p.høyre;
         } else break;         // verdi funnet. Returner.
      }

      if (p == null) return false;  // finner ikke verdi

      if (p.venstre == null && p.høyre == null && p == rot) {
         rot = null;
         antall--;
         return true;
      }

      if (p.venstre == null || p.høyre == null) {   // Tilfelle p har ett eller ingen barn.
         Node<T> b;

         //Setter høyre eller venstre til barnenode.
         if (p.venstre != null) b = p.venstre;
         else b = p.høyre;

         //Hvis peker er rot.
         if (p == rot) {
            rot = b;          // p var rot -> b blir rot.
            b.forelder = null;
         } else if (p == q.venstre && b != null) {
            q.venstre = b;    // p var venstrebarn -> b blir venstrebarn
            b.forelder = q;
         } else if (p == q.høyre && b != null) {
            q.høyre = b;      // p var høyrebarn -> b blir høyrebarn
            b.forelder = q;
         } else {             // p var verken rot eller høyre/venstrebarn -> p fjernes.
            q.høyre = q.venstre = null;
         }

      } else { // Tilfelle p har 2 barn.
         Node<T> s = p, r = p.høyre;

         while (r.venstre != null) {   // Neste inorden.
            s = r;    // s er forelder til r
            r = r.venstre;
         }

         p.verdi = r.verdi;   // kopierer verdien i r til p

         if (s != p) {
            s.venstre = r.høyre;
         } else {
            s.høyre = r.høyre;
         }
      }

      antall--;   // det er nå én node mindre i treet
      return true;
   }

   public int fjernAlle(T verdi) {
      int antall = 0;
      while (fjern(verdi)) {
         antall++;
      }
      return antall;
   }

   public int antall(T verdi) {
      Node<T> p = rot;
      int lookAtTheseDubs = 0;

      while (p != null) {
         int cmp = comp.compare(verdi, p.verdi);
         if (cmp < 0) p = p.venstre;
         else {
            if (cmp == 0) lookAtTheseDubs++;
            p = p.høyre;
         }
      }
      return lookAtTheseDubs;
   }

   // Nullstill tatt fra kompendie 5.2.8
   public void nullstill() {
      // Metodekallet kjører så lenge det tom() returnerer false.
      if (!tom()) nullstill(rot);
      rot = null;
      antall = 0;

   }

   private void nullstill(Node<T> p) {
      // Reskrusive kallet laget stacker der den traverserer fra bunn til topp og sletter høyre og venstre ettersom de ikke har bladnoder.
      if (p.venstre != null) {
         nullstill(p.venstre);
         p.venstre = null;
      }
      if (p.høyre != null) {
         nullstill(p.høyre);
         p.høyre = null;
      }
      p.verdi = null;
   }

   // Metoden tatt fra Programkode 5.1.7 h)
   private static <T> Node<T> førstePostorden(Node<T> p) {
      while (true) {
         if (p.venstre != null) {
            p = p.venstre;

         } else if (p.høyre != null) {
            p = p.høyre;
         } else {
            return p;
         }
      }
   }

   private static <T> Node<T> nestePostorden(Node<T> p) {
      Node<T> q = p.forelder;
      // Ser etter om noden har forelder. Om ikke så er dette roten og vil ikke ha noen neste.
      if (q == null) return null;
      // Ser etter om man skal traversere for å returnere foreldernoden ved å sjekke om det p er foreldrens siste/høyre bladnode.
      if (p == q.høyre || q.høyre == null) return q;
      // Dersom det foreldre har en høyre node, se om dette er bladnode. Hvis ikke gå dypere ned.

      Node<T> q_høyre = q.høyre;
      while (q_høyre.venstre != null) {
         q_høyre = q_høyre.venstre;
      }

      while (q_høyre.høyre != null) {
         q_høyre = q_høyre.høyre;
      }
      return q_høyre;

   }

   public void postorden(Oppgave<? super T> oppgave) {
      //Instansierer peker p til rotnode,
      Node<T> p = førstePostorden(rot);
      //Oppretter Stringjoiner med inndelingsparamatre.
      StringJoiner s = new StringJoiner(" ", "< ", ">");

      //Løkke for å legge sammen alle p.verdi postorden.
      while (p != null) {
         oppgave.utførOppgave(p.verdi);
         s.add(p.verdi.toString());
         p = nestePostorden(p);
      }

   }

   public void postordenRecursive(Oppgave<? super T> oppgave) {
      if (!tom()) postordenRecursive(rot, oppgave);
   }

   // Denne kodesnutten er tatt fra kompendiet.
   private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
      // Skrive rut alle venstre nodeverdier.
      if (p.venstre != null) postordenRecursive(p.venstre, oppgave);
      // Skriver ut alle høyre nodeverdier.
      if (p.høyre != null) postordenRecursive(p.høyre, oppgave);
      // Oppgaven utføres
      oppgave.utførOppgave(p.verdi);
   }

   //Løsningen inspirert av forelsningsvideo

   public ArrayList<T> serialize() {
      //Oppretter arraylist, deque og setter inn rot-node.
      ArrayList<T> liste = new ArrayList<>();
      ArrayDeque<Node<T>> queue = new ArrayDeque<>();
      queue.addLast(rot);

      // Looper gjennom og legger til høyre og venstre noder i arraylisten i en "orderly fashion" 😉.
      while (!queue.isEmpty()) {
         Node<T> p = queue.removeFirst();
         if (p.venstre != null) {
            queue.addLast(p.venstre);
         }
         if (p.høyre != null) {
            queue.addLast(p.høyre);
         }
         liste.add(p.verdi);
      }
      return liste;
   }

   static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
      //Oppretter SBinTre for å legge tilbake fra arraylist.
      SBinTre<K> output = new SBinTre<>(c);
      for (int i = 0; i < data.size(); i++) {
         output.leggInn(data.get(i));
      }
      return output;
   }

} // ObligSBinTre
