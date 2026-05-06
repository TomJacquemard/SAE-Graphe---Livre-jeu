public class Page{
    protected int numero;
    protected String contenu;

    public Page(int numero, String contenu){
        this.numero = numero;
        this.contenu = contenu;
    }

    public int getNumero(){
        return this.numero;
    }

    public void lire(){
        System.out.println(this.contenu);
    }
}