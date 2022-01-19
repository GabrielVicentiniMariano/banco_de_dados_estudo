package com.example.banco_de_dados_estudo.db

import android.annotation.SuppressLint
import android.app.Person
import android.content.ContentValues
import android.content.Context//context= manipular telas e navegação
import android.database.Cursor//cursor= serve para a manipulação de dados
import android.database.sqlite.SQLiteDatabase//Biblioteca do banco de dados(vem com definições do banco de dados)
import android.database.sqlite.SQLiteOpenHelper// SqLiteOpenHelper =Verifica se há erros.

class DatabaseHandler(ctx:Context): SQLiteOpenHelper(ctx,DB_NAME,null,DB_VERSION){
    override fun onCreate(p0: SQLiteDatabase?) {//p0 é a variavl criada para o  banco de dados
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT, $GENDER TEXT, $BIRTH TEXT);"//Comando que o banco de dados vai executar, armazenado em uma variavél  Obs: Integer = Inteiro
 p0?.execSQL(CREATE_TABLE)//Criou/executou a tabela no banco de dados.
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {//onUpgrade=serve para mudar a tabela, mas neste caso ele está usando para zerar e criar outra novamente.
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME;"//Criando uma variavél para excluir a tabela se existir a tabela
        p0?.execSQL(DROP_TABLE)//Excluindo a tabela do banco de dados
        onCreate(p0)//Cria novamente com devidas alterações
    }
//Adicionando Pessoa
fun addPerson(person: com.example.banco_de_dados_estudo.model.Person) {
    val p0 = writableDatabase//Incluir um registro no banco de dados
    //val values = ContentValues().apply { , este comando faz o preenchimento da tabela.
   val values = ContentValues().apply {
       put(NAME, person.name)//Escreve no campo NAME(que está no banco de dados), o conteúdo da classe name
       put(GENDER, person.gender)//Escreve no campo GENDER(que está no banco de dados), o conteúdo da classe gender
       put(BIRTH, person.birth)//Escreve no campo BIRTH(que está no banco de dados), o conteúdo da classe birth
    }
p0.insert(TABLE_NAME, null, values)//Inserindo os dados no banco de dados
}
    //Lendo Pessoa
    fun gerPerson(id: Int ): com.example.banco_de_dados_estudo.model.Person{
        val p0 = readableDatabase//Declarando a Leitura do Banco de Dados
     val  selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID= $id;"//Lendo a tabela  através do Id. Obs:WHERE = usado para pesquisa de dados especifícos.
        val cursor = p0.rawQuery(selectQuery, null)//Lendo uma linha da tabela
        cursor?.moveToFirst()//Trazer o registro para a memória, levar até o cursor[variavél][Poderia ter outro nome](que fica na memória)
        val person = populatePerson(cursor)//Preencher a classe data Person
        cursor.close()//limpando o cursor e liberando a memória
        return person//Devolvendo o person para a tela no xml.
    }
    //Lendo lista de pessoas
    fun getPersonList(): ArrayList<com.example.banco_de_dados_estudo.model.Person> {
        val personList = ArrayList<com.example.banco_de_dados_estudo.model.Person>()
        val p0 = readableDatabase//Declarando a Leitura do Banco de Dados
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $NAME;"//Lendo a tabela inteira ordenada por nome[ordem alfabética], se quiser ler na ordem descrescente, na frente do nome escreva DESC    .
        val cursor = p0.rawQuery(selectQuery, null)//Lendo todas as linhas da tabela
        if (cursor != null) {//Se o cursor for diferente de nulo
            if (cursor.moveToFirst()) {//Se houver registro no cursor
                do {
                    val person = populatePerson(cursor)
                    personList.add(person)//Adiciona na lista do person, para fazer a leitura
                } while (cursor.moveToNext())//Cursor vai para o próximo registro/linha
            }
        }
        cursor.close()//limpando o cursor e liberando a memória
        return personList//Devolvendo o person para a tela no xml.
    }

    //atualizando pessoa
    fun updatePerson(person: com.example.banco_de_dados_estudo.model.Person){
        val p0= writableDatabase
        val values = ContentValues().apply {
            put(NAME, person.name)//Escreve no campo NAME(que está no banco de dados), o conteúdo da classe name
            put(GENDER, person.gender)//Escreve no campo GENDER(que está no banco de dados), o conteúdo da classe gender
            put(BIRTH, person.birth)//Escreve no campo BIRTH(que está no banco de dados), o conteúdo da classe birth
        }
        p0.update(TABLE_NAME, values,"$ID=?", arrayOf(person.id.toString()))//Atualizar a linha cujo o id é igual a um número
    }
    //deletando pessoa
    fun delPerson(id: Int){
        val p0= writableDatabase
      p0.delete(TABLE_NAME,"$ID=?", arrayOf(id.toString()))//Deleta a linha cujo o id é igual a um número.
    }
    //procurando pessoa
    fun searchPerson(str: String) : ArrayList<com.example.banco_de_dados_estudo.model.Person>{
        val personList = ArrayList<com.example.banco_de_dados_estudo.model.Person>()
        val p0 = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $NAME LIKE '%$str%' OR $GENDER LIKE '%$str%' ORDER BY $NAME;"//Procura similares da tabela de acordo com o que foi escrito e pesquisado, ordenado pelo nome signifca que ficará em ordem alfabética.
        val cursor = p0.rawQuery(selectQuery, null)//executando comando anterior
        if (cursor != null) {//Se o cursor achar algum registro, sem erro na sintaxe
            if (cursor.moveToFirst()) {//Se o cursor achar algum registro, porque havia este conteúdo
                do {
                    val person = populatePerson(cursor)
                    personList.add(person)//coloca em uma lista todos os elementos que ele conseguiu ler na tabela
                } while (cursor.moveToNext())//Enquanto existirem registros vai para o próximo
            }
        }
        cursor.close()//limpando o cursor e liberando a memória
        return personList//Devolvendo o person para a tela no xml.
    }
//Populando person o qual foi conectado com o companion object e através dele[companion object] foi criado a tabela que está no banco de dados.

@SuppressLint("Range")
fun populatePerson(cursor: Cursor): com.example.banco_de_dados_estudo.model.Person {
    val person =com.example.banco_de_dados_estudo.model.Person()
        person.id = cursor.getInt(cursor.getColumnIndex(ID))
        person.name = cursor.getString(cursor.getColumnIndex(NAME))
        person.birth = cursor.getString(cursor.getColumnIndex(BIRTH))
        person.gender = cursor.getString(cursor.getColumnIndex(GENDER))
        return person
    }


    companion object{//Companion object = Serve para identificar as tabelas[funções de cada uma] e seus respectivos nomes
        private val DB_VERSION = 1// Versão do banco de dados
        private val DB_NAME = "CadUware"// CadUware = Nome do banco de dados
        private val TABLE_NAME = "Person"//Person = Nome da tabela
        private val ID = "Id"// Id = chave única que identifica cada elemento em cada tabela
        private val NAME = "Name"// Name = 1 Elemento da tabela
        private val GENDER = "Gender"//Gender = 1 Elemento da tabela
        private val BIRTH = "Birth"//Birth = 1 Elemento da tabela

    }
}
