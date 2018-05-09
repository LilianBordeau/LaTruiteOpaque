<?php
// pour tester ce script sans rien installer : http://rextester.com/l/php_online_compiler
$ratioHauteurPlateau=0.8; // premiere valeur a changer : part de la hauteur de la fenetre occupee par le plateau
$ratioLargeurPlateau=0.7122; // deuxieme valeur a changer : part de la largeur de la fenetre occupee par le plateau
$largeur="800.0";
$hauteur="600.0";
$hauteurPlateau=$hauteur*$ratioHauteurPlateau;
$largeurPlateau=$largeur*$ratioLargeurPlateau;
$ratioBordureHaut=(1.0-$ratioHauteurPlateau)/2.0;
$yPlateau=$ratioBordureHaut*$hauteur;
$ratioBordureGauche=(1.0-$ratioLargeurPlateau)/2.0;
$xPlateau=$ratioBordureGauche*$largeur;
$hauteurQuartCase = $hauteurPlateau/25.0;
$hauteurCase=4.0*$hauteurQuartCase;
$largeurCase=$largeurPlateau/8.0;
for( $i=0 ; $i<=7 ; $i++ )
{
    $y=$yPlateau+$i*3.0*$hauteurQuartCase;
    for( $j=0 ; $j<=(($i%2==0)?6:7) ; $j++ )
    {
        $x=$xPlateau+$j*$largeurCase+(($i%2==0)?$largeurCase/2.0:0);
        echo('           <ImageView fx:id="c'.$i.'_'.$j.'" fitHeight="'.$hauteurCase.'" fitWidth="'.$largeurCase.'" layoutX="'.$x.'" layoutY="'.$y.'" pickOnBounds="false" onMouseClicked="#clicTuile"  />
        ');

        echo('           <ImageView fx:id="a'.$i.'_'.$j.'" fitHeight="'.$hauteurCase.'" fitWidth="'.$largeurCase.'" layoutX="'.$x.'" layoutY="'.$y.'" onMouseClicked="#clicTuile"  />
        ');
    }
}
for( $i=0 ; $i<=7 ; $i++ )
{
    $y=$yPlateau+$i*3.0*$hauteurQuartCase;
    for( $j=0 ; $j<=(($i%2==0)?6:7) ; $j++ )
    {
        $x=$xPlateau+$j*$largeurCase+(($i%2==0)?$largeurCase/2.0:0);
        echo('           <ImageView fx:id="p'.$i.'_'.$j.'" fitHeight="'.(0.75*$hauteurCase).'" fitWidth="'.($largeurCase/2.0).'" layoutX="'.($x+(0.25*$hauteurCase)).'" layoutY="'.$y.'" pickOnBounds="false"  smooth="true" preserveRatio="true" onMouseClicked="#clicTuile" />
        ');

    }
}
