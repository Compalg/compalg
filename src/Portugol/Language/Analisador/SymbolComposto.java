package Portugol.Language.Analisador;

import Portugol.Language.Calcular.Calculador;
import Portugol.Language.Criar.ExpandDefinirSimbol;
import Portugol.Language.Criar.Intermediario;
import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Utilitario.IteratorArray;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class SymbolComposto extends Simbolo {
    
    Vector<Simbolo> Campos;
    TipoRegisto tipoRegistoBase;
    
    public SymbolComposto(String modify, String type, String name, /*String index,*/ Object valor, int level, String origTxt)
            throws LanguageException {
        TextoOrigen = origTxt;
        if (modify.equalsIgnoreCase(Keyword.GetTextKey(Keyword.CONSTANTE))) {
            this.isConst = true;
        } else {
            this.isConst = false;
        }
        
        this.type = getType(type);
        typeLexema = type;//parametro

        this.name = name.trim();
        
        this.value = Values.getDefault(typeLexema);
        
        this.level = level;
        
        tipoRegistoBase = ObterTipoRegisto(type);
        CriaCampos();
        if (valor != null && valor instanceof SymbolComposto) {
            copyFrom((SymbolComposto) valor);
        }
    }

    /**
     *
     * @param symbolComposto
     */
    public SymbolComposto(SymbolComposto symbol) throws LanguageException {
        TextoOrigen = symbol.TextoOrigen;
        isConst = symbol.isConst;
        type = symbol.type;
        typeLexema = symbol.typeLexema;//parametro

        this.name = symbol.name.trim();
        
        this.level = symbol.level;
        
        tipoRegistoBase = symbol.tipoRegistoBase;
        
        Campos = new Vector<Simbolo>();
        for (int i = 0; i < symbol.Campos.size(); i++) {
            if (symbol.Campos.get(i) instanceof SymbolArray) {
                Campos.add(new SymbolArray((SymbolArray) symbol.Campos.get(i)));
            } else if (symbol.Campos.get(i) instanceof SymbolComposto) {
                Campos.add(new SymbolComposto((SymbolComposto) symbol.Campos.get(i)));
            } else {
                Campos.add(new Simbolo(symbol.Campos.get(i)));
            }
        }
    }
    
    public void copyFrom(SymbolComposto registro) throws LanguageException {
        Campos.clear();
        for (int i = 0; i < registro.Campos.size(); i++) {
            if (registro.Campos.get(i) instanceof SymbolArray) {
                Campos.add(new SymbolArray((SymbolArray) registro.Campos.get(i)));
            } else if (registro.Campos.get(i) instanceof SymbolComposto) {
                Campos.add(new SymbolComposto((SymbolComposto) registro.Campos.get(i)));
            } else {
                Campos.add(new Simbolo(registro.Campos.get(i)));
            }
        }
    }
    
    public void copyFrom(SimboloDeParametro origen) throws LanguageException {
        if (origen == null) {
            return;
        }
//        if (origen.PorValor) {
//            throw new LanguageException("Tipo de parâmetro não é equivalente ao esperado", "Mude o tipo de parâmetro na chamada");//David:Revisar
//        }
        if (!typeEqual((Simbolo) origen.Value)) {
            throw new LanguageException("Tipo de parâmetro não é equivalente ao esperado", "Mude o tipo de parâmetro na chamada");//David:Revisar
        }
        SymbolComposto registro = (SymbolComposto) origen.Value;
        copyFrom(registro);
    }

    //-----------------------------------------------------------------------
//    public void setValue(String val) throws LanguageException {
//        if (this.isConst) {
//            throw new LanguageException("O SIMBOLO " + this.name + " É UMA CONSTANTE, POR ISSO, NÃO PODE RECEBER VALOR",
//                    " ALTERE O SIMBOLO PARA VARIÁVEL, SE FOR O TEU OBJECTIVO");
//        }
//        this.value = getNormalizedValue(val);
//        Campos.set(0, this.value);
//    }
//    /**
//     * retorna o valor
//     *
//     * @return valor
//     */
//    @Override
//    public Object getValue() {
//        return (String) Campos.get(0);
//    }
    public static TipoRegisto ObterTipoRegisto(String type) throws LanguageException {
        for (int i = 0; i < Intermediario.tiposRegistos.size(); i++) {
            TipoRegisto tmp = (TipoRegisto) Intermediario.tiposRegistos.get(i);
            if (tmp.Name.toUpperCase().equals(type.toUpperCase().trim())) {
                return tmp;
            }
        }
        throw new LanguageException(
                "O tipo de Registo não foi declarado",
                "Declare o tipo de registo"); //David: Revisar ortografia                        
    }

    //////////////////////////////////////////////////////////////////////////////
    private void CriaCampos() throws LanguageException {
        NodeInstruction pt = null;
        Campos = new Vector();
        for (int i = 0; i < tipoRegistoBase.Defs.size(); i++) {
            Simbolo v = (Simbolo) tipoRegistoBase.Defs.get(i);
            //String text = v.isConstant() ? "CONSTANTE " : " ") + v.typeLexema + " " + v.getName() + " <- "
            //        + (v instanceof SymbolComposto ? v.typeLexema : v.getValue());
            pt = new NodeInstruction(v.TextoOrigen, 0, 0);
            ExpandDefinirSimbol.ExpandVariable(pt, 0, Campos);
        }
    }
    
    private void CopiarValor() {
    }
    
    public String toString() {
        return name + " = " + Campos.toString();
    }

    /**
     * nome =
     *
     * @param var varivel a comparar
     * @return nome = paramentro
     */
    public boolean typeEqual(Simbolo otro) {
        if (otro instanceof SymbolComposto) {
            return tipoRegistoBase.Name.equals(((SymbolComposto) otro).tipoRegistoBase.Name);
        } else {
            return false;
        }
    }
}
