﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SerializarJSON
{
    public class Personaje
    {
        public String nom { get; set; }
        public string frase { get; set; }
        public string rutaImagen { get; set; }

        public Personaje()
        {

        }

        public Personaje(string nom, string frase)
        {
            this.nom = nom;
            this.frase = frase;
        }
    }
}
