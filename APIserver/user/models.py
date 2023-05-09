from django.db import models

# Create your models here.


class Account(models.Model):
    name = models.CharField(max_length=50)
    id = models.CharField(primary_key=True, max_length=50)
    key = models.CharField(max_length=128)
    password = models.CharField(max_length=128)
    email = models.EmailField(max_length=128)

