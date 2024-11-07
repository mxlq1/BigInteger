import java.math.BigInteger;
import java.util.ArrayList;

class BigInt {
    private final ArrayList<Integer> digits = new ArrayList<>();
    private boolean sign = true;
    public BigInt(String str_input){
        int i = 0;  // счетчик для перебора строки
        int j = 0;  // счетчик для  добавления в массив
        if (str_input.charAt(0) == '-'){
            this.sign = false;
            i++;
        }

        while (i < str_input.length()){

            digits.add(j, str_input.charAt(i) - '0');
            i++;
            j++;
        }
    }

    // Далее идут 3 доп. метода для более простой работы add и subtract: сложение и вычитание по модулю и сравнение по модулю

    public BigInt absAddition(BigInt second_number){ //|a| + |b|
        String str = "";
        int number = 0; // number в уме

        int first = this.digits.size() - 1; // указатель первого числа
        int second = second_number.digits.size() - 1; // указатель второго числа

        while (first >= 0 && second >= 0){

            number = this.digits.get(first) + second_number.digits.get(second) + number;
            str = (number % 10) + str;
            number /= 10;

            first--;
            second--;

        }

        while (first >= 0){

            number = this.digits.get(first) + number;
            str = (number % 10) + str;
            number /= 10;

            first--;

        }

        while (second >= 0){

            number = second_number.digits.get(second) + number;
            str = (number % 10) + str;
            number /= 10;

            second--;

        }

        while (number != 0){

            str = number + str;
            number /= 10;

        }

        return new BigInt(str);
    }

    public BigInt absSubtraction(BigInt second_number){ // |a| - |b|, |a| >= |b|

        String str = "";
        int number = 0;
        int first = this.digits.size() - 1; // указатель первого числа
        int second = second_number.digits.size() - 1; // указатель второго числа

        while (first >= 0 && second >= 0){

            if (this.digits.get(first) + number < second_number.digits.get(second)){ // если нужно занять еденицу
                str = (10 + this.digits.get(first) - second_number.digits.get(second) + number) + str;
                number = -1;
            }
            else{ // если не надо ничего занимать
                str = (this.digits.get(first) - second_number.digits.get(second) + number) + str;
                number = 0;
            }
            first--;
            second--;

        }

        while (first >= 0){

            if (this.digits.get(first) + number < 0){ // если надо занять

                str = (10 + number) + str;
                number = -1;
            }
            else{ // если занимать не надо + учитываем займ предыдущего

                str = this.digits.get(first) + number + str;
                number = 0;
            }
            first--;
        }

        return new BigInt(str.replaceFirst("^0+(?!$)", "")); // убираем ведущие нули
    }


    public boolean absIsGreaterOrEqual(BigInt second_number){ // |a|?|b|

        if (this.digits.size() > second_number.digits.size()){return true;}
        if (this.digits.size() < second_number.digits.size()){return false;}

        for (int i = 0; i < second_number.digits.size(); i++){
            if (this.digits.get(i).equals(second_number.digits.get(i))){continue;}
            if (this.digits.get(i) > second_number.digits.get(i)){return true;}
            if (this.digits.get(i) < second_number.digits.get(i)){return false;}
        }

        return true; //equal
    }



    @Override
    public String toString(){
        String str_output = "";

        if (!sign){
            str_output += "-";
        }

        for (int i = 0; i < this.digits.size(); i++){
            str_output += this.digits.get(i);
        }

        if  (str_output.equals("-0")){
            return "0";
        }

        return str_output;
    }


    public int compareTo(BigInt second_number){

        if (this.sign && !second_number.sign){return 1;} // + -
        if (!this.sign && second_number.sign){return -1;} // - +

        if (this.sign && this.digits.size() > second_number.digits.size()){return 1;} // 100 > 1
        else if (this.sign && this.digits.size() < second_number.digits.size()){return -1;} // 1 < 100
        if (!this.sign && this.digits.size() > second_number.digits.size()){return -1;} // -100 < -1
        else if (!this.sign && this.digits.size() < second_number.digits.size()){return 1;} // -1 > -100

        for (int i = 0; i < this.digits.size(); i++){ // числа одиноковой длины, неважно до чьего size() идти

            if (this.digits.get(i).equals(second_number.digits.get(i))){continue;}
            if (this.digits.get(i) > second_number.digits.get(i)){return this.sign ? 1: -1;}
            if (this.digits.get(i) < second_number.digits.get(i)){return this.sign ? -1: 1;}
        }

        return 0;
    }


    public int absCompareTo(BigInt second_number){ // для деления, чтобы знак не учитывал - |a| ? |b|

        if (this.sign && this.digits.size() > second_number.digits.size()){return 1;} // 100 > 1
        else if (this.sign && this.digits.size() < second_number.digits.size()){return -1;} // 1 < 100

        for (int i = 0; i < this.digits.size(); i++){ // числа одиноковой длины, неважно до чьего size() идти

            if (this.digits.get(i).equals(second_number.digits.get(i))){continue;}
            if (this.digits.get(i) > second_number.digits.get(i)){return 1;}
            else{return -1;}
        }

        return 0;
    }


    public static BigInt valueOf(long input_value){
        return new BigInt(Long.toString(input_value));
    }


    public BigInt add(BigInt second_number){

        if (this.sign && second_number.sign){ // ++
            return this.absAddition(second_number);
        }

        if (!this.sign && !second_number.sign){ //--
            BigInt result = this.absAddition(second_number);
            result.sign = false;
            return result;
        }

        if (this.sign && !second_number.sign){ // +-

            if (this.absIsGreaterOrEqual(second_number)){return this.absSubtraction(second_number);}

            else{
                BigInt result = second_number.absSubtraction(this);
                result.sign = false;
                return result;
            }

        }

        if (!this.sign && second_number.sign){ // -+

            if (this.absIsGreaterOrEqual(second_number)){
                BigInt result = this.absSubtraction(second_number);
                result.sign = false;
                return result;
            }

            else{return second_number.absSubtraction(this);}

        }

        return BigInt.valueOf(0); // чтобы не ругался компилятор, мог бы последний if заменить на else, но так читабильнее
    }


    public BigInt subtract(BigInt second_number){

        if (!this.sign && second_number.sign){ // -+
            BigInt result = this.absAddition(second_number);
            result.sign = false;
            return result;
        }
        if (this.sign && !second_number.sign){return this.absAddition(second_number);} //+-

        if (this.sign && second_number.sign){ // ++

            if (this.absIsGreaterOrEqual(second_number)){return this.absSubtraction(second_number);}

            else{
                BigInt result = second_number.absSubtraction(this);
                result.sign = false;
                return result;
            }

        }

        if (!this.sign && !second_number.sign){ // --

            if (this.absIsGreaterOrEqual(second_number)){
                BigInt result = this.absSubtraction(second_number);
                result.sign = false;
                return result;
            }

            else{return second_number.absSubtraction(this);}

        }

        return BigInt.valueOf(0);
    }


    public BigInt multiply(BigInt second_number){
        int number = 0; // number в уме
        String str = "";
        BigInt sum = new BigInt("0"); // промежуточная сумма
        int indent = 0; // отступ для каждой промежуточной суммы

        for (int i = this.digits.size() - 1; i >= 0; i--){
            for (int j = second_number.digits.size() - 1; j >= 0; j--){

                int res = this.digits.get(i) * second_number.digits.get(j) + number;
                str = (res % 10) + str;
                number = res / 10;

            }
            str = number + str; // при последеней итерации все, что в уме, переносим в само число
            str = str.replaceFirst("^0+(?!$)", ""); // убираем ведущие нули
            number = 0;

            sum = sum.absAddition(new BigInt(str + "0".repeat(indent)));
            indent += 1;
            str = "";

        }

        BigInt result = new BigInt(String.valueOf(sum));
        result.sign = this.sign == second_number.sign;
        return result;
    }


    public BigInt divide(BigInt second_number){

        if (second_number.compareTo(BigInt.valueOf(1)) == 0){return this;} // x/1 = x
        if (second_number.compareTo(BigInt.valueOf(-1)) == 0){ // x/-1 = -x or x
            BigInt result = this;
            result.sign = !this.sign;
            return result;
        }

        BigInt middle_res = new BigInt("0");
        String str = "";

        for (int i = 0; i < this.digits.size(); i++){
            middle_res = middle_res.absAddition(new BigInt(this.digits.get(i).toString()));
            int j = 0;

            while (j <= 10){ // ищем цифру частного
                BigInt a = BigInt.valueOf(j).multiply(second_number);
                int comparison = middle_res.absCompareTo(a);

                if (comparison == -1){
                    j--;
                    break;
                }
                j++;
            }

            str += j;
            middle_res = middle_res.absSubtraction(second_number.multiply(new BigInt(String.valueOf(j)))); // находим остаток
            middle_res = middle_res.multiply(new BigInt("10")); // сдвигаем разряд
        }

        str = str.replaceFirst("^0+(?!$)", "");
        BigInt result = new BigInt(str);
        result.sign = this.sign == second_number.sign;
        return result;
    }
}
