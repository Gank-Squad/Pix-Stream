package uwu.nyaa.owo.finalproject.data.db;

/**
 * exists but not used, was gonna store user stuff, but we didn't have time
 * @author minno
 *
 */
public class TableUsers
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_users";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_users("
            + "user_id serial PRIMARY KEY, "
            + "username VARCHAR UNIQUE NOT NULL, "
            + "password bytea NOT NULL,"
            + "is_admin boolean NOT NULL, "
            + "is_disabled boolean NOT NULL"
            + ");";
    
    
    /*
     Have a Shondo, if you ever make it this far into our backend.
     
                                 .,;;;;;;;;'.
                        ,:cc;,,::::::;:loc;
                     ,ll;;:cccccllllllcc:;,cl'
                :O.'o:,::clccccllllllllllc;;,:d.
                  o:;clllll:lclllllcclcllll:;;,ll
                 d,,;llcllcllcllllllcclcllllc;;;,x
                ,:,:clclllclllolllooo:clllcllc;;;,lc::;
                x';clccll;lolloloooooollllccllc;;;,cc:,c
             .;o,,ccl:llllooolooooodoolkoll:lll;cc,:c:c
           lol::''lcl:lloddddooooddddddOdoo:loll:c:',:
           ';:cc',lcl:oodkodxdxxdddxxdxk0oolcooo;c,..;
             :;:',lco:oodKolxxxOO0O00xkNNxdlcdoo;;'.c
              ,..'lco:ookOo:oXWWWWWWW;...coloddd:;;.O
               l.'cloo;lo,,::KWWWWWWWxodoKOdxdoo,c::o.
               .c;,lldocOOkO0WWWWWWWWWK0KOldc,::::::;klkd
               k;;;;c;lolOKXWWWWWNNWWWNX0dol:;lcc:lc:cl
           oOdd:;c;:::::cx00XWWWK0KKWWK0KOodcdooocl'';l
            ,0d'ccc:cccllkKK0kKKO0K0OlOXXXdklodoo:'..l.
            'd.','::llllcxKNN00koxxxxc0NWd0dlcdod;...;,
           lc......;dodoclxONWkxx00OkxWWOOkkkodlo,,,,'c
          d:,,,,,,;kldodoOdx00dX0xkKWxxdc0XXko;cl:
                 :' ooooookKKOkO0NKO0xKN0XNXok;l:
                     ;looxWWWxxKKKKXxxWWX0XOKkc
                    .d:llWWWXlxxxxdd;lx0XcdkOxO.
                .:clc;:cXNNNxookxOooxOdlc;lll:,clc;.
              ,ol;:colcKXXXXcooxdxooONXOdcllll;;;:cO:
              cxx:0oooxKKKXxlllllllldOkxo:olcll:::xx.
                 klxxkoKKKx:cclcccc:;:::cokxodxllk.
                 ;Oxooodl.c;;;;;;;,;;:ll:ocoxddx0c
              :col:;;;,'.,oxxdolcclllol:''',,;;;;cod.
              x:;;;;,'...cloooodxoddxdloc.o0XNOl:ccl
               o:;,..'''''''.','.''.;xoxc,,oO:d0clo
               d,..''''.',;;,'''',,,''x;x;,,,,,,,';lc:;;;;;;;;'
              o:.''''.',;;;;,'.,,,,,'',,,..',,,,,,'.,,',,,,:,kx,
       .';;;cc.',,,'',;;;;;;''',,,,,.',;;;,..,',,',:cccc::l:oxx
   .lcll;,,,,,;;,,,,;;;;;;;,',;,,,,,'';;;;;;',;;;;;cccc:cl:oOxx
  Oxoc,;:;;;;,,,;;;;::::;;;';;;;;,;,',;;;:::cc:;;;;::cllccox;
    xkx:,;:;,;:::;;::::::;,:::;;;;;,,,:::cccc:cl;;::cc;loo:O,
      .oolc'::;;;:::::::::ccccc:;;;;;;:cccclollod;cc:cxll::;lo
          d,,:dxdc;:cccc:ooc:cc:::::::::coxx:lcc:;clox;:::::::x.
         cl',,co:dxdlc::odolodolllccccodxdxdcldldk0x.l::::::::;o;
        'o,,,;;;;;;:Okxdllllc:xd:xkkkkxO0OOko:        occc::ccc:d'
        k,;;;;:::::occoododooooclOOOOOOOOOOxl          ,c:cc::lll
       c:::ccccccl   ;;,,,,'''',oodddxxxddoo
                      dclllloododdooooooolx
                      0cdddddoc,lddddxddll
     */
}
