package org.ditw.pise5.misc;


public java.lang.Object generate(Object[] references) {
        return new SpecificUnsafeProjection(references);
        }

class SpecificUnsafeProjection extends org.apache.spark.sql.catalyst.expressions.UnsafeProjection {

    private Object[] references;
    private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[] mutableStateArray_0 =
            new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[2];

    public SpecificUnsafeProjection(Object[] references) {
        this.references = references;
        mutableStateArray_0[0] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(1, 32);
        mutableStateArray_0[1] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(mutableStateArray_0[0], 1);
    }

    public void initialize(int partitionIndex) {

    }

    // Scala.Function1 need this
    public java.lang.Object apply(java.lang.Object row) {
        return apply((InternalRow) row);
    }

    public UnsafeRow apply(InternalRow i) {
        mutableStateArray_0[0].reset();

        mutableStateArray_0[0].zeroOutNullBytes();

        boolean isNull_0 = i.isNullAt(0);
        InternalRow value_0 = isNull_0 ?
                null : (i.getStruct(0, 1));
        if (isNull_0) {
            mutableStateArray_0[0].setNullAt(0);
        } else {
            final InternalRow tmpInput_0 = value_0;
            if (tmpInput_0 instanceof UnsafeRow) {
                mutableStateArray_0[0].write(0, (UnsafeRow) tmpInput_0);
            } else {
                // Remember the current cursor so that we can calculate how many bytes are
                // written later.
                final int previousCursor_0 = mutableStateArray_0[0].cursor();

                mutableStateArray_0[1].resetRowWriter();


                if ((tmpInput_0.isNullAt(0))) {
                    mutableStateArray_0[1].setNullAt(0);
                } else {
                    mutableStateArray_0[1].write(0, (tmpInput_0.getUTF8String(0)));
                }


                mutableStateArray_0[0].setOffsetAndSizeFromPreviousCursor(0, previousCursor_0);
            }
        }
        return (mutableStateArray_0[0].getRow());
    }


}