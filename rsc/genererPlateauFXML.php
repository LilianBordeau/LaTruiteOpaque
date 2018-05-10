<?php

// Générateur de plateau
// Tester le script sans ne rien installer : http://rextester.com/l/php_online_compiler

// Part de la fenetre occupée par le plateau
$ratioHauteurPlateau = 0.8;    
$ratioLargeurPlateau = 0.7122;

// Taille de la fenêtre
$largeur = "800.0";
$hauteur = "600.0";

// Taille du plateau avec le ratio
$hauteurPlateau = $hauteur*$ratioHauteurPlateau; 
$largeurPlateau = $largeur*$ratioLargeurPlateau;

// Bordure du plateau avec le ratio
$ratioBordureHaut=(1.0-$ratioHauteurPlateau)/2.0;
$ratioBordureGauche=(1.0-$ratioLargeurPlateau)/2.0;

// Origine du plateau avec bordure
$yPlateau=$ratioBordureHaut*$hauteur;
$xPlateau=$ratioBordureGauche*$largeur;

$hauteurQuartCase = $hauteurPlateau/25.0;

// Taille d'une case
$hauteurCase=4.0*$hauteurQuartCase;
$largeurCase=$largeurPlateau/8.0;

// Génération des cases simples et des cases accessibles
for( $i=0 ; $i<=7 ; $i++ )
{
    $y=$yPlateau+$i*3.0*$hauteurQuartCase;
    for( $j=0 ; $j<=(($i%2==0)?6:7) ; $j++ )
    {
        $x=$xPlateau+$j*$largeurCase+(($i%2==0)?$largeurCase/2.0:0);
        echo('
                <ImageView fx:id="c'.$i.'_'.$j.'" fitHeight="'.$hauteurCase.'" fitWidth="'.$largeurCase.'" layoutX="'.$x.'" layoutY="'.$y.'" pickOnBounds="false" onMouseClicked="#clicTuile"/>');
        echo('
                <ImageView fx:id="a'.$i.'_'.$j.'" fitHeight="'.$hauteurCase.'" fitWidth="'.$largeurCase.'" layoutX="'.$x.'" layoutY="'.$y.'" onMouseClicked="#clicTuile"/>');
    }
}

// Génération des pingouins
for( $i=0 ; $i<=7 ; $i++ )
{
    $y=$yPlateau+$i*3.0*$hauteurQuartCase;
    for( $j=0 ; $j<=(($i%2==0)?6:7) ; $j++ )
    {
        $x=$xPlateau+$j*$largeurCase+(($i%2==0)?$largeurCase/2.0:0);
        echo('
                <ImageView fx:id="p'.$i.'_'.$j.'" fitWidth="'.($largeurCase/2.0).'" layoutX="'.($x+(0.25*$hauteurCase)).'" layoutY="'.$y.'" pickOnBounds="false" smooth="true" preserveRatio="true" onMouseClicked="#clicTuile"/>');
    }
}

?>
